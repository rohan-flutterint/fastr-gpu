
## ASTx
## Mandelbrot benchmark 

## Parse arguments
args <- commandArgs(trailingOnly=TRUE)

if (length(args) == 0) {
	stop("No input size passed. Usage: ./mandelbrotSeq.R <size> ")
} 

size <- as.integer(args[1])

REPETITIONS <-11

space <- 2/size

benchmark <- function(inputSize) {

	mandelbrotFunction <- function(indexIDX, indexJDX) {
		iterations <- 10000
		Zr <- 0
        Zi <- 0
        Cr <- (1 * indexJDX * space - 1.5)
        Ci <- (1 * indexIDX * space - 1.0)

        ZrN <- 0
        ZiN <- 0
        y <- 0

		while ((y < iterations) && ((ZiN + ZrN) <= 4)) {
			Zi <- 2.0 * Zr * Zi + Ci;
            Zr <- 1 * ZrN - ZiN + Cr;
            ZiN <- Zi * Zi;
            ZrN <- Zr * Zr;
			y <- y + 1
		}

		result <- ((y * 255) / iterations);
		return(result)
	}

	totalSize <- size*size
	x <- flagSequence(1, size, size)
	y <- compassSequence(1, size, size)
		
	for (i in 1:REPETITIONS) {
		start <- nanotime()
		result <- marawacc.sapply(x, mandelbrotFunction, nThreads=1, y);
		end <- nanotime()
		total <- end - start
		print(paste("Total Time:" , total))
	}
}

## Main
print("FASTR CPU")
print(paste("SIZE:", size))
benchmark(size)

