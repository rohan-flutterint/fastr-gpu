# ChangeLog FastR-GPU Compiler and Execution Environment

## fastr-gpu-0.13
	* Marawacc Multi-Device support imported

## fastr-gpu-0.12
	* Rsequence improved for OpenCL data transfer optimization (flag and compass sequences)
	* Refactor and documentation improved
	* OpenCL deoptimization technique improved (resume from last element in the AST interpreter)

## fastr-gpu-0.11
	* Option for node rewriting for scope detection
	* New Parray Strategy for primitives arrays such as int[] or double[], marshal is zero overhead.
	* RSequence with the new PArray primitives
	* Deoptimisation technique improved (maximum number of deopts and node rewriting supported)	
	* Buffer reallocation when needed for the same input function
	* MApply parallel version supported 

## fastr-gpu-0.10
	* Deoptimizations supported with a flag in OpenCL
	* New scope phases
	* Truffle annotation system for OpenCL node filtering and deopt
	* Javadoc
	* Refactor of the main OpenCL builtin

## fastr-gpu-0.9
	* Garbage collector builtin provided
	* R root node for OpenCL execution equivalent to normal FastR
	* Profiling information improved
	* Benchmarks updated 

## fastr-gpu-0.8 
	* Mandelbrot supported
	* Lexical scope variables improved
	* Cache included for all the parameters related to the RFunction
	* Profiling info added 

## fastr-gpu-0.7
	* NBody, SpectralNorm, DFT and Euler benchmarks supported
	* Scope by pattern in the IR detected and use it in the code generation
	* PArrays introduced 
	* RSequence optimisation 

## fastr-gpu-0.6
	* Graph (GraalIR) phases with node simplification.
	* KMeans suported
	* Basic scope supported for KMeans and general pattern detection in the IR

## fastr-gpu-0.5 
	* Benchmarks saxpy, montecarlo and blackscholes provided. There is an assumption in montecarlo for random numbers (still working on it).
	* Nodes ZeroExtendNode, XorNode and OrNode supported.
	* ForeignCallNode fixed.
	* AMD64MathIntrinsicNode supported.
	* Builtin for random numbers in Java
	* Fix semantic Tuples and RLists

## fastr-gpu-0.4 
	* Tuples support from RList in the R lambda expression
	* Graph simplified within the interpreter level to the GraalIR
	* Graal extension to get the information whithin the data types from Partial Escape Analysis
	* Mararawcc code generator improved for tuples support from Virtual Nodes and CommitAllocationNode

## fastr-gpu-0.3
	* Basic support for GPU from AST -> Graal IR with Truffle information -> OpenCL code.
      This is just one example workin (examples/gpu/testGPU.R) 
	* NA assumptions. There is no GPU code support for NA numbers. 
	* Depth assumption is 0 -> It is removed from the code generator.
	* Graph simplification phases. It removes FrameState nodes from the original graph.
	* Boxing and UnBoxing elimination. 

## fastr-gpu-0.2a
	* Basic support for kernel generation (simple expressions) and non-generic.
      This is a proof of concept.
	* Interception from the Partial Evaluator to the GPU backend
	* Mechanism to identify a potencial R callTarget for GPU compilation

## fastr-gpu-0.2
	* Support async for map operations : get((map(map(map))))
	* Support async for reduction operations
	* Get node added
	* Futures added for async support
	* Change number of threads per expression

## fastr-gpu-0.1
	* Support for map/reduce with Marawacc threads backend. 
	* Support for Integer, Boolean and Double arrays
	* Function composition from R.


