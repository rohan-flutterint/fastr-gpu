
## AST-X Compiler
## Hilbert benchmark 

args <- commandArgs(trailingOnly=TRUE)

if (length(args) == 0) {
	stop("No input size passed. Usage: ./hilbertGPUSequence.R <size> ")
} 

size <- as.integer(args[1])

REPETITIONS <-11
CHECK_RESULT <- T

benchmark <- function(inputSize) {

	hilbertGPUFunction <- function(x, y) {
		aux <- x + y - 1
		value <- 1.0 / aux
		return(value)
	}	

	hilbertCPUFunction <- function(x, y) {
		aux <- x + y - 1
		value <- 1.0 / aux
		return(value)
	}	

	totalSize <- size*size
	x <- flagSequence(1, size, size)
	y <- compassSequence(1, size, size)

	if (CHECK_RESULT) {
		resultSeq <- mapply(hilbertCPUFunction, x, y)
	}

	for (i in 1:REPETITIONS) {
		system.gc()
		start <- nanotime()
		result <- marawacc.testGPU(x, hilbertGPUFunction, y)
		total <- nanotime() - start
		print(paste("Total Time: ", total))

		if (CHECK_RESULT) {
			for (j in 1:totalSize) {
				if (abs(resultSeq[j] - result[j]) > 0.1) {
					print("Result is wrong")
					break
				}
			}
		}
	}
}

## Main 
print("FASTR CPU")
print(paste("SIZE:", size))
benchmark(size)

