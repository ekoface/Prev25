package compiler.phase.regall;

import java.util.*;
import compiler.phase.asmgen.Instr;
import compiler.phase.livean.LivenessAnalyzer;
import compiler.phase.imclin.LIN;

public class Regall {
    private final Map<String, Set<String>> interferenceGraph;
    private final List<String> registers;
    private final List<Instr> instructions;

    private final Map<String, String> allocation = new HashMap<>();
    private final Set<String> spilled = new HashSet<>();

    public Regall(Map<String, Set<String>> interferenceGraph, List<String> registers, List<Instr> instructions) {
        this.interferenceGraph = interferenceGraph;
        this.registers = registers;
        this.instructions = instructions;
    }

    public void allocateRegisters(LivenessAnalyzer livenessAnalyzer, LIN.CodeChunk codeChunk) {
    Stack<String> stack = new Stack<>();
    Set<String> graphNodes = new HashSet<>(interferenceGraph.keySet());
    Map<String, Set<String>> graphCopy = deepCopy(interferenceGraph);

    // Special handling for FP and RV
    String fpTemp = codeChunk.frame.FP.toString();
    String rvTemp = codeChunk.frame.RV.toString();

    // Assign FP to s0
    //allocation.put(fpTemp, "s0");

    // Assign RV to a0
    //cdallocation.put(rvTemp, "a0");

    // Simplify step
    while (!graphNodes.isEmpty()) {
        boolean removed = false;

        for (String node : new HashSet<>(graphNodes)) {
            // Skip FP and RV since they are already assigned
            if (node.equals(fpTemp) || node.equals(rvTemp)) {
                graphNodes.remove(node);
                continue;
            }

            Set<String> neighbors = graphCopy.getOrDefault(node, Collections.emptySet());
            int degree = (int) neighbors.stream().filter(graphNodes::contains).count();

            if (degree < registers.size()) {
                stack.push(node);
                graphNodes.remove(node);
                for (String neighbor : neighbors) {
                    graphCopy.get(neighbor).remove(node);
                }
                removed = true;
                break;
            }
        }

        if (!removed) {
            // Ensure graphNodes is not empty before accessing its iterator
            if (!graphNodes.isEmpty()) {
                String toSpill = graphNodes.iterator().next();
                spilled.add(toSpill);
                stack.push(toSpill);
                graphNodes.remove(toSpill);
                for (String neighbor : graphCopy.getOrDefault(toSpill, Collections.emptySet())) {
                    graphCopy.get(neighbor).remove(toSpill);
                }
            }
        }
    }

    // Select step
    while (!stack.isEmpty()) {
        String var = stack.pop();

        // Skip FP and RV since they are already assigned
        if (var.equals(fpTemp) || var.equals(rvTemp)) {
            continue;
        }

        Set<String> neighbors = interferenceGraph.getOrDefault(var, Collections.emptySet());

        Set<String> usedRegs = new HashSet<>();
        for (String neighbor : neighbors) {
            if (allocation.containsKey(neighbor)) {
                usedRegs.add(allocation.get(neighbor));
            }
        }

        String assigned = null;
        for (String reg : registers) {
            if (!usedRegs.contains(reg)) {
                assigned = reg;
                break;
            }
        }

        if (assigned != null) {
            allocation.put(var, assigned);
        } else {
            spilled.add(var);
        }
    }
}

    public Map<String, String> getAllocation() {
        return allocation;
    }

    public Set<String> getSpilled() {
        return spilled;
    }

    private Map<String, Set<String>> deepCopy(Map<String, Set<String>> original) {
        Map<String, Set<String>> copy = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : original.entrySet()) {
            copy.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        return copy;
    }
}