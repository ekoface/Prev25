package compiler;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;

import compiler.common.report.*;
import compiler.phase.lexan.*;
import compiler.phase.synan.*;
import compiler.phase.abstr.*;
import compiler.phase.seman.*;
import compiler.phase.memory.*;
import compiler.phase.imcgen.*;
import compiler.phase.imclin.*;
import compiler.phase.asmgen.*;
import compiler.phase.livean.*;
import compiler.phase.regall.*;
import compiler.phase.finalsol.*;

import java.util.Scanner;

/**
 * The Prev25 compiler.
 * 
 * @author bostjan.slivnik@fri.uni-lj.si
 */
public class Compiler {

	/** (Unused but included to keep javadoc happy.) */
	private Compiler() {
		throw new Report.InternalError();
	}

	/** Names of command line options. */
	private static final HashSet<String> cmdLineOptNames = new HashSet<String>(Arrays.asList("--src-file-name",
			"--dst-file-name", "--target-phase", "--logged-phase", "--xml", "--xsl", "--dev-mode", "--num-regs"));
	private static final int DEFAULT_NUM_REGS = 16;
	/** Values of command line options indexed by their command line option name. */
	private static final HashMap<String, String> cmdLineOptValues = new HashMap<String, String>();

	/** All valid phases name of the compiler. */
	private static final Vector<String> phaseNames = new Vector<String>(
			Arrays.asList("none", "all", "lexan", "synan", "abstr", "seman", "memory", "imcgen", "imclin"));

	/**
	 * Returns the value of a command line option.
	 *
	 * @param cmdLineOptName Command line option name.
	 * @return Command line option value.
	 */
	public static final String cmdLineOptValue(final String cmdLineOptName) {
		return cmdLineOptValues.get(cmdLineOptName);
	}

	/** Specifies whether the compiler is run in the development mode. */
	private static boolean devMode = false;

	/**
	 * Returns information on whether the compiler is run in the development mode.
	 * 
	 * @return {@code true} if the compiler is run in the development mode,
	 *         {@code false} otherwise.
	 */
	public static final boolean devMode() {
		return devMode;
	}

	/**
	 * The compiler's main driver running all phases one after another.
	 * 
	 * @param opts Command line arguments (see {@link compiler}).
	 */
	public static void main(final String[] opts) {
		try {
			Report.info("This is Prev25 compiler:");

			// Scan the command line.
			for (int optc = 0; optc < opts.length; optc++) {
				if (opts[optc].startsWith("--")) {
					// Command line option.
					final String cmdLineOptName = opts[optc].replaceFirst("=.*", "");
					final String cmdLineOptValue = opts[optc].replaceFirst("^[^=]*=", "");
					if (!cmdLineOptNames.contains(cmdLineOptName)) {
						Report.warning("Unknown command line option '" + cmdLineOptName + "'.");
						continue;
					}
					if (cmdLineOptValues.get(cmdLineOptName) == null) {
						// Not yet successfully specified command line option.

						// Check the value of the command line option.
						if (cmdLineOptName.equals("--target-phase") && (!phaseNames.contains(cmdLineOptValue))) {
							Report.warning("Illegal phase specification in '" + opts[optc] + "' ignored.");
							continue;
						}
						if (cmdLineOptName.equals("--logged-phase") && (!phaseNames.contains(cmdLineOptValue))) {
							Report.warning("Illegal phase specification in '" + opts[optc] + "' ignored.");
							continue;
						}
						if (cmdLineOptName.equals("--dev-mode") && (!cmdLineOptValue.matches("on|off"))) {
							Report.warning("Illegal value in '" + opts[optc] + "' ignored.");
							continue;
						}

						cmdLineOptValues.put(cmdLineOptName, cmdLineOptValue);
					} else {
						// Repeated specification of a command line option.
						Report.warning("Command line option '" + opts[optc] + "' ignored.");
						continue;
					}
				} else {
					// Source file name.
					if (cmdLineOptValues.get("--src-file-name") == null) {
						cmdLineOptValues.put("--src-file-name", opts[optc]);
					} else {
						Report.warning("Source file '" + opts[optc] + "' ignored.");
						continue;
					}
				}
			}
			// Check the command line option values.
			if (cmdLineOptValues.get("--src-file-name") == null) {
				try {
					// Source file has not been specified, so consider using the last modified
					// prev25 file in the working directory.
					final String currWorkDir = new File(".").getCanonicalPath();
					FileTime latestTime = FileTime.fromMillis(0);
					Path latestPath = null;
					for (final Path path : java.nio.file.Files.walk(Paths.get(currWorkDir))
							.filter(path -> path.toString().endsWith(".prev25")).toArray(Path[]::new)) {
						final FileTime time = Files.getLastModifiedTime(path);
						if (time.compareTo(latestTime) > 0) {
							latestTime = time;
							latestPath = path;
						}
					}
					if (latestPath != null) {
						cmdLineOptValues.put("--src-file-name", latestPath.toString());
						Report.warning("Source file not specified, using '" + latestPath.toString() + "'.");
					}
				} catch (final IOException __) {
					throw new Report.Error("Source file not specified.");
				}

				if (cmdLineOptValues.get("--src-file-name") == null) {
					throw new Report.Error("Source file not specified.");
				}
			}
			if (cmdLineOptValues.get("--dst-file-name") == null) {
				cmdLineOptValues.put("--dst-file-name",
						// TODO: Insert the appropriate file suffix.
						cmdLineOptValues.get("--src-file-name").replaceFirst("\\.[^./]*$", ".TODO"));
			}
			if (cmdLineOptValues.get("--target-phase") == null)
				cmdLineOptValues.put("--target-phase", "all");
			if (cmdLineOptValues.get("--logged-phase") == null)
				cmdLineOptValues.put("--logged-phase", "none");
			devMode = ("on".equals(cmdLineOptValues.get("--dev-mode")));

			// Carry out the compilation phase by phase.
			while (true) {

				if (cmdLineOptValues.get("--target-phase").equals("none"))
					break;

				// Lexical analysis.
				if (cmdLineOptValues.get("--target-phase").equals("lexan")) {
					try (final LexAn lexan = new LexAn()) {
						while (lexan.lexer.nextToken().getType() != LexAn.LocLogToken.EOF) {
						}
					}
					break;
				}

				// Syntax analysis.
				try (LexAn lexan = new LexAn(); SynAn synan = new SynAn(lexan)) {
					SynAn.tree = synan.parser.source();
					synan.log(SynAn.tree);
				}
				if (cmdLineOptValues.get("--target-phase").equals("synan"))
					break;

				// Abstract syntax.
				try (Abstr abstr = new Abstr()) {
					Abstr.tree = (AST.Nodes<AST.FullDefn>) SynAn.tree.ast;
					SynAn.tree = null;
					Abstr.Logger logger = new Abstr.Logger(abstr.logger);
					Abstr.tree.accept(logger, "Nodes<Defn>");
				}
				if (cmdLineOptValues.get("--target-phase").equals("abstr"))
					break;

				// Semantic analysis.
				try (SemAn seman = new SemAn()) {
					Abstr.tree.accept(new NameResolver(), null);
					Abstr.tree.accept(new TypeResolver(), null);
					Abstr.tree.accept(new TypeChecker(), null);
					Abstr.Logger logger = new Abstr.Logger(seman.logger);
					logger.addSubvisitor(new SemAn.Logger(seman.logger));
					Abstr.tree.accept(logger, "Nodes<Defn>");
				}
				if (cmdLineOptValues.get("--target-phase").equals("seman"))
					break;

				// Memory.
				try (Memory memory = new Memory()) {
					Abstr.tree.accept(new MemEvaluator(), null);
					Abstr.Logger logger = new Abstr.Logger(memory.logger);
					logger.addSubvisitor(new SemAn.Logger(memory.logger));
					logger.addSubvisitor(new Memory.Logger(memory.logger));
					Abstr.tree.accept(logger, "Nodes<Defn>");
				}
				if (cmdLineOptValues.get("--target-phase").equals("memory"))
					break;

				// Intermediate code generation.
				try (ImcGen imcGen = new ImcGen()) {
					Abstr.tree.accept(new ImcGenerator(), null);
					Abstr.Logger logger = new Abstr.Logger(imcGen.logger);
					logger.addSubvisitor(new SemAn.Logger(imcGen.logger));
					logger.addSubvisitor(new Memory.Logger(imcGen.logger));
					logger.addSubvisitor(new ImcGen.Logger(imcGen.logger));
					Abstr.tree.accept(logger, "AstDefn");
				}
				if (cmdLineOptValues.get("--target-phase").equals("imcgen"))
					break;

				// Linearization of intermediate code.
				String numRegsStr = Compiler.cmdLineOptValue("--num-regs");
				int numRegs = (numRegsStr != null) ? Integer.parseInt(numRegsStr) : DEFAULT_NUM_REGS;
				try (ImcLin imclin = new ImcLin()) {
					Abstr.tree.accept(new ChunkGenerator(), null);
					imclin.log();

					if (false) {
						Interpreter interpreter = new Interpreter(ImcLin.dataChunks(), ImcLin.codeChunks());
						System.out.println("EXIT CODE: " + interpreter.run("_main"));
					}
					AsmGen asmGen = new AsmGen(ImcLin.codeChunks());
					// for (LIN.CodeChunk codeChunk : codeChunks) {
					if (false){
						for (LIN.CodeChunk codeChunk : ImcLin.codeChunks()) {
							List<Instr> new_list = asmGen.getInstructionsForCodeChunk(codeChunk);
							System.out.println("Instructions for code chunk: " + codeChunk.frame.label.name);
							for (Instr instr : new_list) {
								System.out.println(instr);
							}
							System.out.println("---------------------------");
							
						}
					}
					// Perform liveness analysis
					LivenessAnalyzer analyzer = new LivenessAnalyzer(asmGen.getCodeChunkToInstructions());
					//analyzer.printLivenessInfo();
					//analyzer.printInterferenceGraph();

					// Define available registers

					List<String> availableRegisters = Arrays.asList("t1", "t2", "t3", "t4", "t5", "t6", "a3", "a2", "s1", "s2", "s3", "s4", "s5", "s6", "s7");
					// get only the first numRegs registers
					if (numRegs > availableRegisters.size()) {
						numRegs = availableRegisters.size();
					}
					availableRegisters = availableRegisters.subList(0, numRegs);
					// Initialize SpillRegulator
					SpillRegulator spillRegulator = new SpillRegulator(analyzer, availableRegisters);

					// Process each chunk for spills and register allocation
					spillRegulator.processChunks();

					// After processing, retrieve the updated instructions
					Map<LIN.CodeChunk, List<Instr>> updatedInstructions = spillRegulator.getUpdatedInstructions();

					// Print the updated instructions for each chunk
					if (false){
						for (Map.Entry<LIN.CodeChunk, List<Instr>> entry : updatedInstructions.entrySet()) {
							LIN.CodeChunk codeChunk = entry.getKey();
							List<Instr> instructions = entry.getValue();

							//System.out.println("Updated Instructions for function: " + codeChunk.frame.label.name);
							//for (Instr instr : instructions) {
							//	System.out.println(instr);
							//}
							//System.out.println("---------------------------");
						}
					}
					Map<LIN.CodeChunk, Long> max_spills = spillRegulator.getSpillOffsetsMax();
					CompF compf = new CompF(updatedInstructions, ImcLin.dataChunks(),spillRegulator,max_spills);
					List<String> finalSolution = compf.getFinalInstructions();

					// Print the final assembly code
					//System.out.println("Final Assembly Code:");
					// fill the finalSolution in check.txt
					try (BufferedWriter writer = new BufferedWriter(new FileWriter("_final_code.txt"))) {
						for (String instr : finalSolution) {
							writer.write(instr);
							writer.newLine();
						}
						System.out.println("Final assembly code written to _final_code.txt");
					} catch (IOException e) {
						System.err.println("Error writing to file: " + e.getMessage());
					}
					
					// if (false){
					// 	List<Instr> instructions = asmGen.getInstructions();
					// 	LivenessAnalyzer livenessAnalyzer = new LivenessAnalyzer(instructions);
					// // Print the generated RISC-V instructions.
					// }
					// if (false){
					// 		System.out.println("RISC-V Instructions:");
					// 	for (Instr instr : instructions) {
					// 		instr.printDefAndUse();
							
					// 	}
					// 	System.out.println("RISC-V Instructions done.");
					// 	System.out.println("---------------------------");
					// }
					// if(false){
					// 	// Print the liveness information
					// 	System.out.println("Liveness Analysis:");
					// 	livenessAnalyzer.printLivenessInfo();
					// 	System.out.println("Liveness Analysis done.");
					// 	System.out.println("---------------------------");
					// 	Map<String, Set<String>> interferenceGraph = livenessAnalyzer.buildInterferenceGraph();
					// 	livenessAnalyzer.printInterferenceGraph(interferenceGraph);
					// 	System.out.println("Interference Graph done.");
					// 	System.out.println("---------------------------");
					// }
					// if (false){
					// 	Scanner scanner = new Scanner(System.in);
					// 	System.out.print("Enter input: ");
					// 	int numRegisters = scanner.nextInt();
					// 	scanner.nextLine();
					// 	List<String> availableRegisters = new ArrayList<>();
					// 	for (int i = 1; i <= numRegisters +1; i++) {
					// 		availableRegisters.add("$" + i);
					// 	}
					// 	Map<String, Set<String>> interferenceGraph = livenessAnalyzer.buildInterferenceGraph();
					// 	Regall regall = new Regall(interferenceGraph, availableRegisters, instructions);
					// 	regall.allocateRegisters();
					// 	System.out.println("Spillled Variables:");
					// 	System.out.println(regall.getSpilled());
					// 	System.out.println("-----------------------------");
					// 	SpillRegulator spillRegulator = new SpillRegulator(interferenceGraph, availableRegisters, instructions);
					// 	spillRegulator.checkForSpills();
					// 	System.out.println("Spill Regulator done.");
					// 	// Print updated instructions
					// 	System.out.println("Updated Instructions:");
					// 	List<Instr> updatedInstructions = spillRegulator.getInstructions();
					// 	for (Instr instr : updatedInstructions) {
					// 		System.out.println(instr);
					// 	}
						//CompF compf= new CompF(updatedInstructions, imclin);
						//compf.compile();
						//List<String> final_solution = compf.getFinalInstructions();
						//for (String instr : final_solution) {
						//	System.out.println(instr);
						//}
					//}

					// Perform register allocation and update instructions
					
					
				}
				//read from the command line
				
				
				if (cmdLineOptValues.get("--target-phase").equals("imclin"))
					break;
				// Do not loop... ever.
				break;
			}

			// Let's hope we ever come this far.
			// But beware:
			// 1. The generated translation of the source file might be erroneous :-o
			// 2. The source file might not be what the programmer intended it to be ;-)
			Report.info("Done.");
		} catch (final Report.Error error) {
			System.err.println(error.getMessage());
			System.exit(1);
		}
	}

}
