package compiler.phase.livean;

import java.util.*;
import compiler.phase.asmgen.Instr;
import compiler.phase.imclin.LIN;

public class LivenessAnalyzer {

    private final Map<LIN.CodeChunk, List<Instr>> codeChunkToInstructions;
    private final Map<Instr, InstrLiveness> livenessMap = new HashMap<>();
    private final Map<LIN.CodeChunk, List<InstrLiveness>> chunkToLiveness = new HashMap<>();
    private final Map<LIN.CodeChunk, Map<String, Set<String>>> chunkToInterferenceGraph = new HashMap<>();

    public LivenessAnalyzer(Map<LIN.CodeChunk, List<Instr>> codeChunkToInstructions) {
        this.codeChunkToInstructions = codeChunkToInstructions;

        // Perform liveness analysis and build interference graph for each CodeChunk
        for (Map.Entry<LIN.CodeChunk, List<Instr>> entry : codeChunkToInstructions.entrySet()) {
            LIN.CodeChunk codeChunk = entry.getKey();
            List<Instr> instructions = entry.getValue();

            // Compute liveness for this CodeChunk
            computeLivenessForChunk(codeChunk, instructions);

            // Build the interference graph for this CodeChunk
            buildInterferenceGraphForChunk(codeChunk);
        }
    }

    private void computeLivenessForChunk(LIN.CodeChunk codeChunk, List<Instr> instructions) {
    Map<Instr, Set<Instr>> successors = computeSuccessors(instructions);

    // Initialize liveness map for this chunk
    List<InstrLiveness> livenessList = new ArrayList<>();
    for (Instr instr : instructions) {
        InstrLiveness liveness = new InstrLiveness(instr, codeChunk);
        livenessMap.put(instr, liveness);
        livenessList.add(liveness);
    }
    chunkToLiveness.put(codeChunk, livenessList);

    String fpTemp = codeChunk.frame.FP.toString();
    String rvTemp = codeChunk.frame.RV.toString();

    boolean changed;
    do {
        changed = false;

        for (int i = instructions.size() - 1; i >= 0; i--) {
            Instr instr = instructions.get(i);
            InstrLiveness liveness = livenessMap.get(instr);

            Set<String> oldIn = new HashSet<>(liveness.getIn());
            Set<String> oldOut = new HashSet<>(liveness.getOut());

            // Recompute out[n] = union of in[s] for all successors s of n
            liveness.getOut().clear();
            for (Instr succ : successors.getOrDefault(instr, Set.of())) {
                liveness.getOut().addAll(livenessMap.get(succ).getIn());
            }
            liveness.getOut().remove(fpTemp); // Exclude FP
            liveness.getOut().remove(rvTemp); // Exclude RV

            // Recompute in[n] = use[n] ∪ (out[n] - def[n])
            Set<String> use = getUse(instr);
            Set<String> def = getDef(instr);

            liveness.getIn().clear();
            liveness.getIn().addAll(use);
            for (String var : liveness.getOut()) {
                if (!def.contains(var)) {
                    liveness.getIn().add(var);
                }
            }
            liveness.getIn().remove(fpTemp); // Exclude FP
            liveness.getIn().remove(rvTemp); // Exclude RV

            if (!oldIn.equals(liveness.getIn()) || !oldOut.equals(liveness.getOut())) {
                changed = true;
            }
        }
    } while (changed);
}

    private void buildInterferenceGraphForChunk(LIN.CodeChunk codeChunk) {
        Map<String, Set<String>> interferenceGraph = new HashMap<>();

        // Get the liveness information for this chunk
        List<InstrLiveness> livenessList = chunkToLiveness.get(codeChunk);

        // Track all variables (def, use) in the function
        Set<String> allVariables = new HashSet<>();

        for (InstrLiveness liveness : livenessList) {
            Set<String> outSet = liveness.getOut();
            Set<String> defSet = getDef(liveness.getInstr());
            Set<String> useSet = getUse(liveness.getInstr());

            // Add all variables to the set of all variables
            allVariables.addAll(defSet);
            allVariables.addAll(useSet);

            // Add edges between all variables in the out set
            for (String var1 : outSet) {
                for (String var2 : outSet) {
                    if (!var1.equals(var2)) {
                        addEdge(interferenceGraph, var1, var2);
                    }
                }
            }

            // Add edges between def and all variables in the out set
            for (String def : defSet) {
                for (String out : outSet) {
                    if (!def.equals(out)) {
                        addEdge(interferenceGraph, def, out);
                    }
                }
            }
        }

        // Ensure all variables are nodes in the graph
        for (String var : allVariables) {
            interferenceGraph.putIfAbsent(var, new HashSet<>());
        }
        // add FP and RV
        interferenceGraph.putIfAbsent(codeChunk.frame.FP.toString(), new HashSet<>());
        interferenceGraph.putIfAbsent(codeChunk.frame.RV.toString(), new HashSet<>());

        // Store the interference graph for this chunk
        chunkToInterferenceGraph.put(codeChunk, interferenceGraph);
}

    private void addEdge(Map<String, Set<String>> graph, String var1, String var2) {
        graph.computeIfAbsent(var1, k -> new HashSet<>()).add(var2);
        graph.computeIfAbsent(var2, k -> new HashSet<>()).add(var1);
    }

    private Map<Instr, Set<Instr>> computeSuccessors(List<Instr> instructions) {
        Map<Instr, Set<Instr>> successors = new HashMap<>();
        Map<String, Instr> labelMap = new HashMap<>();

        // Map labels to instructions
        for (Instr instr : instructions) {
            if (instr instanceof Instr.Label label) {
                labelMap.put(label.toString(), instr);
            }
        }

        for (int i = 0; i < instructions.size(); i++) {
            Instr instr = instructions.get(i);
            Set<Instr> succs = new HashSet<>();

            if (instr instanceof Instr.Oper oper) {
                String op = oper.operation;
                List<String> uses = oper.getUse();

                if ("j".equals(op) || "jal".equals(op)) {
                    if (!uses.isEmpty()) {
                        Instr target = labelMap.get(uses.get(0));
                        if (target != null) succs.add(target);
                    }
                } else if ("beq".equals(op) || "bne".equals(op)) {
                    if (uses.size() >= 3) {
                        Instr trueTarget = labelMap.get(uses.get(2));
                        if (trueTarget != null) succs.add(trueTarget);
                    }
                    if (i + 1 < instructions.size()) {
                        succs.add(instructions.get(i + 1));
                    }
                } else {
                    if (i + 1 < instructions.size()) {
                        succs.add(instructions.get(i + 1));
                    }
                }
            } else {
                if (i + 1 < instructions.size()) {
                    succs.add(instructions.get(i + 1));
                }
            }

            successors.put(instr, succs);
        }

        return successors;
    }

    private Set<String> getUse(Instr instr) {
        if (instr instanceof Instr.Oper oper) {
            return new HashSet<>(oper.getUse());
        } else if (instr instanceof Instr.Move move) {
            return move.getUse() != null ? Set.of(move.getUse()) : Set.of();
        }
        return Set.of();
    }

    private Set<String> getDef(Instr instr) {
        if (instr instanceof Instr.Oper oper) {
            return oper.getDef() != null ? Set.of(oper.getDef()) : Set.of();
        } else if (instr instanceof Instr.Move move) {
            return move.getDef() != null ? Set.of(move.getDef()) : Set.of();
        }
        return Set.of();
    }

    public void printInterferenceGraph() {
        for (Map.Entry<LIN.CodeChunk, Map<String, Set<String>>> entry : chunkToInterferenceGraph.entrySet()) {
            LIN.CodeChunk codeChunk = entry.getKey();
            Map<String, Set<String>> interferenceGraph = entry.getValue();

            System.out.println("Interference Graph for function: " + codeChunk.frame.label.name);
            for (Map.Entry<String, Set<String>> graphEntry : interferenceGraph.entrySet()) {
                String var = graphEntry.getKey();
                Set<String> neighbors = graphEntry.getValue();
                System.out.println(var + " -> " + neighbors);
            }
            System.out.println("---------------------------");
        }
    }

    public void printLivenessInfo() {
        for (Map.Entry<LIN.CodeChunk, List<InstrLiveness>> entry : chunkToLiveness.entrySet()) {
            LIN.CodeChunk codeChunk = entry.getKey();
            List<InstrLiveness> livenessList = entry.getValue();

            System.out.println("Function: " + codeChunk.frame.label.name);
            for (InstrLiveness liveness : livenessList) {
                liveness.printLiveness();
            }
            System.out.println("---------------------------");
        }
    }
    //.getInterferenceGraphForChun
    public Map<String, Set<String>> getInterferenceGraphForChunk(LIN.CodeChunk codeChunk) {
        return chunkToInterferenceGraph.getOrDefault(codeChunk, Collections.emptyMap());
    }
    //getCodeChunkToInstructions();
    public Map<LIN.CodeChunk, List<Instr>> getCodeChunkToInstructions() {
        return codeChunkToInstructions;
    }
    public void recomputeLivenessForChunk(LIN.CodeChunk codeChunk, List<Instr> updatedInstructions) {
        // Update the instructions for the given CodeChunk
        codeChunkToInstructions.put(codeChunk, updatedInstructions);

        // Recompute liveness for the updated instructions
        computeLivenessForChunk(codeChunk, updatedInstructions);

        // Rebuild the interference graph for the updated instructions
        buildInterferenceGraphForChunk(codeChunk);
    }
    
    
}