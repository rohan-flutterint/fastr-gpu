#! /usr/bin/Python

import os 

def inspectCurrentVersion():
	command = "cat mx.fastr/suite.py | grep -A1  marawacc | grep version > tmp_marawacc_version"
	os.system(command)

def compareVersions():
	fLast = open("marawacc_version")
	last = fLast.read()
	
	fCurrent = open("tmp_marawacc_version")
	current = fCurrent.read()

	if (last != current):
		print "Update repository: make update"

def removeTempFile():
	command = "rm tmp_marawacc_version"
	os.system(command)

if __name__ == "__main__":
	inspectCurrentVersion()	
	compareVersions()
	removeTempFile()