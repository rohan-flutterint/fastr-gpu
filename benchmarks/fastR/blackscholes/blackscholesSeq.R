
## AST-X Compiler
## Blackcholes benchmark

## Parse arguments
args <- commandArgs(trailingOnly=TRUE)

if (length(args) == 0) {
	stop("No input size passed. Usage: ./blackcholesSeq.R <size> ")
} 

size <- as.integer(args[1])

REPETITIONS <-11

CHECK <- FALSE

## Lambda expression for the computation
benchmark <- function(inputSize) {

	bsFunction <- function(x) {
		inRand <- x;
        sLOWERLIMIT <- 10.0;
        sUPPERLIMIT <- 100.0;
        kLOWERLIMIT <- 10.0;
        kUPPERLIMIT <- 100.0;
        tLOWERLIMIT <- 1.0;
        tUPPERLIMIT <- 10.0;
        rLOWERLIMIT <- 0.01;
        rUPPERLIMIT <- 0.05;
        sIGMALOWERLIMIT <- 0.01;
        sIGMAUPPERLIMIT <- 0.10;
        s <- sLOWERLIMIT * inRand + sUPPERLIMIT * (1.0 - inRand);
        KConstant <- kLOWERLIMIT * inRand + kUPPERLIMIT * (1.0 - inRand);
        t <- tLOWERLIMIT * inRand + tUPPERLIMIT * (1.0 - inRand);
        r <- rLOWERLIMIT * inRand + rUPPERLIMIT * (1.0 - inRand);
        v <- sIGMALOWERLIMIT * inRand + sIGMAUPPERLIMIT * (1.0 - inRand);

	 	d1 <- (log(s / KConstant) + ((r + (v * v / 2)) * t)) / v * sqrt(t)
		d2 <- d1 - (v * sqrt(t))

	   # cnd(d1)
       a1 <- 0.319381530
       a2 <- -0.356563782
       a3 <- 1.781477937
       a4 <- -1.821255978
       a5 <- 1.330274429
       a6 <- 2.506628273

       l <- abs(d1);
       k <- 1.0 / (1.0 + 0.2316419 * l);

	   w <- 1.0 - 1.0 / 1 * a6 * exp((-1 * l) * l / 2) * (a1 * k + a2 * k * k * 1 + a3 * k * k * k * +a4 * k * k * k * k * 1 + a5 * k * k * k * k * k);
		resultD1 <- w
       if (d1 < 0) {
       		resultD1 <- 1.0 - w;
       }

		# cnd(d2)
        l <- abs(d2);
        k <- 1.0 / (1.0 + 0.2316419 * l);
        w <- 1.0 - 1.0 / 1 * a6 * exp((-1 * l) * l / 2) * (a1 * k + a2 * k * k * 1 + a3 * k * k * k * +a4 * k * k * k * k * 1 + a5 * k * k * k * k * k);
        resultD2 <- w;
        if (d2 < 0) {
        	resultD2 <- 1.0 - w;
        }

		## Call price
       callRes <- s * resultD1 - KConstant * exp(1 * t * (-1) * r) * resultD2;

		# cnd(-1)
            l <- abs(-d1);
            k <- 1.0 / (1.0 + 0.2316419 * l);
            w <- 1.0 - 1.0 / 1 * a6 * exp((-1 * l) * l / 2) * (a1 * k + a2 * k * k * 1 + a3 * k * k * k * +a4 * k * k * k * k * 1 + a5 * k * k * k * k * k);
            resultD1Minus <-  w
            if ((-d1) < 0) {
            	resultD1Minus <- 1.0 - w;
            } 

            # cnd(-2)
            l <- abs(-d2);
            k <- 1.0 / (1.0 + 0.2316419 * l);
            w <- 1.0 - 1.0 / 1 * a6 * exp((-1 * l) * l / 2) * (a1 * k + a2 * k * k * 1 + a3 * k * k * k * +a4 * k * k * k * k * 1 + a5 * k * k * k * k * k);
            resultD2Minus <- w
            if ((-d2) < 0) {
            	resultD2Minus <- 1.0 - w;
			}

			# Put price
            putRes <- KConstant * exp(1 * t * (-1) * r) - resultD2Minus - s * resultD1Minus;
 
		list(callRes, putRes)	
	}	

	
	initialization <- function(size) {
        x <- runif(size, 0, 1)
		return (x)
	}

	x <- initialization(size)

	for (i in 1:REPETITIONS) {
		start <- nanotime()
		result <- marawacc.sapply(x, bsFunction, nThreads=1);
		end <- nanotime()
		total <- end - start
		print(paste("Total time: ", total))
	}
}

## Main 
print("FASTR Sequential")
print(paste("SIZE:", size))
benchmark(size)

