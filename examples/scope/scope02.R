a <- 1:1000

gpuFunction <- function(x) {
	foo <- 0
	s <- scope[10]
	for (i in 1:size) {
		b <- x * s
		foo <- foo + b
	}
	return (foo)
}

# Inputs
size <- 1000
scope <- runif(1000)
#scope <- 1001:2000
a <- 1:size

for (i in seq(1,20)) {
    start <- nanotime()
    result <- marawacc.gpusapply(a, gpuFunction)
    end <- nanotime()
    print(end-start);

}

