a <- 1:100000

f <- function(x) {
	acc <- 0
	for (i in seq(1, 1000)) {
		acc <- acc + x + i
	}
	return (acc)
}

g <- function(x) {
	acc <- 0
	for (i in seq(1, 1000)) {
		acc <- acc + x + i
	}
	return (acc)
}

seqResult <- sapply(a, f)

for (i in seq(1,20)) {

    start <- nanotime()
    result <- marawacc.testGPU(a, f, nThreads=4)
    end <- nanotime()
    print(end-start);
	
	nonError <- identical(seqResult, result)
	if (!nonError) {
		for (i in seq(result)) {
			if (abs(seqResult[i] - result[i]) > 0.1) {
				print(nonError)
				break;
			}
		}	
	} else {
		print(nonError) 
	}

}

for (i in seq(1,20)) {
    start <- nanotime()
    result <- sapply(a, g)
    end <- nanotime()
    print(end-start);
}

