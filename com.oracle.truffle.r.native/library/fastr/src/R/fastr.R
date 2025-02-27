#
# Copyright (c) 2015, 2015, Oracle and/or its affiliates. All rights reserved.
# DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
#
# This code is free software; you can redistribute it and/or modify it
# under the terms of the GNU General Public License version 2 only, as
# published by the Free Software Foundation.
#
# This code is distributed in the hope that it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
# FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
# version 2 for more details (a copy is included in the LICENSE file that
# accompanied this code).
#
# You should have received a copy of the GNU General Public License version
# 2 along with this work; if not, write to the Free Software Foundation,
# Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
#
# Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
# or visit www.oracle.com if you need additional information or have any
# questions.
#

Interop.import <- function(name) invisible(.FastR(.NAME="Interop.import", name))

Interop.export <- function(name, value) invisible(.FastR(.NAME="Interop.export", name, value))

fastr.createcc <- function(func) invisible(.FastR(.NAME="createcc", func))

fastr.getcc <- function(func) .FastR(.NAME="getcc", func)

fastr.compile <- function(func, background=TRUE) .FastR(.NAME="compile", func, background)

fastr.dumptrees <- function(func, igvDump=FALSE, verbose=FALSE) .FastR(.NAME="dumptrees", func, igvDump, verbose)

fastr.syntaxtree <- function(func, source=FALSE, visitAll=FALSE) invisible(.FastR(.NAME="syntaxtree", func, source, visitAll))

fastr.tree <- function(func, verbose=FALSE) invisible(.FastR(.NAME="tree", func, verbose))

fastr.typeof <- function(x) .FastR(.NAME="typeof", x)

fastr.stacktrace <- function(print.frame.contents=TRUE) invisible(.FastR(.NAME="stacktrace", print.frame.contents=FALSE))

fastr.debug <- function(element) invisible(.FastR(.NAME="debug", element))

fastr.inspect <- function(...) invisible(.FastR(.NAME="inspect", ...))

fastr.createpkgsources <- function(pkgs = NULL) {
    if (!length(pkgs)) {
        pkgs <- sub("package:", "", grep("package:", search(), value=TRUE, fixed=TRUE), fixed=TRUE)
    }
    for (pkg in pkgs) {
		.createpkgsource(pkg)
    }
	.FastR(.NAME="pkgsource.done", pkg)
	invisible(NULL)
}

.createpkgsource <- function(pkg, pattern) {
	ns <- asNamespace(pkg)
	if (missing(pattern)) {
		names <- ls(envir=ns, all.names=TRUE)
	} else {
		names <- ls(pattern=pattern, envir=ns, all.names=TRUE)
	}
	for (n in names) {
		.FastR(.NAME="pkgsource.pre", pkg, n)
		val <- get(n, ns)
		.FastR(.NAME="pkgsource.post", pkg, n, val)
	}
}

fastr.createpkgsource <- function(pkg, name) {
	.createpkgsource(pkg, name)
	.FastR(.NAME="pkgsource.done", pkg)
	invisible(NULL)
}

fastr.context.get <- function() {
	.FastR(.NAME="context.get")
}

fastr.context.create <- function(args="", kind="SHARE_NOTHING") {
	kind <- match(kind, c("SHARE_NOTHING", "SHARE_PARENT_RW", "SHARE_PARENT_RO"))
	if (is.na(kind)) stop("invalid kind argument")
	context <- .FastR(.NAME="context.create", args, kind)
	class(context) <- "fastr_context"
    context
}

print.fastr_context <- function(x, ...) {
	.FastR(.NAME="context.print", x)
	invisible(x)
}

fastr.context.spawn <- function(contexts, exprs) {
	.FastR(.NAME="context.spawn", contexts, exprs)
	invisible(NULL)
}

fastr.context.join <- function(contexts) {
	.FastR(.NAME="context.join", contexts)
	invisible(NULL)
}

fastr.context.eval <- function(contexts, exprs, par=FALSE) {
	result = .FastR(.NAME="context.eval", contexts, exprs, par)
	invisible(result)
}

fastr.context.pareval <- function(contexts, exprs) {
	fastr.context.eval(contexts, exprs, par=TRUE)
}

fastr.channel.create <- function(key) {
	.FastR(.NAME="fastr.channel.create", key)
}

fastr.channel.get <- function(key) {
	.FastR(.NAME="fastr.channel.get", key)
}

fastr.channel.close <- function(id) {
	.FastR(.NAME="fastr.channel.close", id)
	invisible(NULL)
}

fastr.channel.send <- function(id, data) {
	.FastR(.NAME="fastr.channel.send", id, data)
	invisible(NULL)
}

fastr.channel.receive <- function(id) {
	.FastR(.NAME="fastr.channel.receive", id)
}

fastr.throw <- function(name) {
	.FastR(.NAME="fastr.throw", name)
}

fastr.trace <- function(what, tracer, exit, at, print, signature, where, edit) {
                              # from = NULL, untrace = FALSE, classMethod = FALSE) {
	 if (is.character(what)) {
		 what <- get(what, envir=where, mode="function")
	 }
     .FastR(.NAME="fastr.trace", what, tracer, exit, at, print, signature, where, edit)#, from, untrace, classMethod)
}

#  ################################################################
#	  		        Marawacc- GPU/CPU Intrinsics 		          #
#  ################################################################

# Generic builtins (utils)
nanotime <- function() {
	.FastR(.NAME="builtin.nanotime")
}

system.gc <- function() {
	.FastR(.NAME="system.gc")
}

random <- function(x) {
	.FastR(.NAME="builtin.random", x)
}

# OpenCL functions
marawacc.init <- function() {
	.FastR(.NAME="marawacc.init")
}

marawacc.isOpenCL <- function() {
	.FastR(.NAME="marawacc.isOpenCL")
}

marawacc.deviceInfo <- function() {
	.FastR(.NAME="marawacc.deviceInfo")
}

marawacc.sapply <- function(input, userFunction, nThreads=1, ...) {
	.FastR(.NAME="marawacc.sapply", input, userFunction, nThreads, ...)
}

marawacc.testGPU <- function(input, userFunction, ...) {
	.FastR(.NAME="marawacc.testGPU", input, userFunction, ...)
}

marawacc.gpusapply <- function(input, userFunction, ...) {
	.FastR(.NAME="marawacc.testGPU", input, userFunction, ...)
}

marawacc.parray <- function(rArray) {
	.FastR(.NAME="marawacc.parray", rArray)
}

# Multi-thread marawacc interface 
marawacc.map <- function(input, userFunction, nThreads=1, ...) {
	.FastR(.NAME="marawacc.map", input, userFunction, nThreads, ...)
}

marawacc.execute <- function(arrayFunction) {
	.FastR(.NAME="marawacc.execute", arrayFunction)
}

marawacc.get <- function(arrayFunction) {
	.FastR(.NAME="marawacc.get", arrayFunction)
}

marawacc.reduce <- function(input, userFunction, neutral=0, ...) {
	.FastR(.NAME="marawacc.reduce", input, userFunction, neutral, ...)
}

# Blocking reduction. It returns the result
marawacc.reduction <- function(input, userFunction, neutral=0, ...) {
	.FastR(.NAME="marawacc.terminalReduce", input, userFunction, neutral, ...)
}

marawacc.vectorMul <- function(a, b) {	
	.FastR(.NAME="marawacc.vectorMul", a, b)
}

# #########################################################################
# ASTx Thread model
# #########################################################################
astx.async <- function(userFunction, input) {
	.FastR(.NAME="astx.async", userFunction, input)
}

astx.sync <- function(arrayThreadsIDs) {
	.FastR(.NAME="astx.sync", arrayThreadsIDs)
}


# #########################################################################
# Experiments
# #########################################################################
mylist <- function() {
	.FastR(.NAME="mylist")
}

compiler.printAST <- function(userFunction) {
	.FastR(.NAME="compiler.printAST", userFunction) 
}


# #########################################################################
# Efficient sequences for OpenCL
# #########################################################################
flagSequence <- function(start, max, repetitions) {
	.FastR(.NAME="flagSequence", start, max, repetitions);
}

compassSequence <- function(start, max, repetitions) {
	.FastR(.NAME="compassSequence", start, max, repetitions);
}

