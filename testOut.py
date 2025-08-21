#!/usr/bin/env python3

import os
import sys
import subprocess as sp
import difflib
import argparse

def print_diff(expected: str, actual: str):
    diff = difflib.unified_diff(expected.splitlines(True), actual.splitlines(True), "EXPECTED", "ACTUAL", n=2)
    
    is_diff = False
    for line in diff:
        is_diff = True
        print(line, end="" if len(line) > 0 and line[-1] == '\n' else '\n')
    
    if not is_diff:
        print("No difference")
        return
    
    if (expected[-1] == '\n' and actual[-1] != '\n') or (expected[-1] != '\n' and actual[-1] == '\n'):
        print("\\ No new line at the end")

def run_test(test: str):
    with sp.Popen(["make", "-s", test], stdout=sp.PIPE) as p:
        p.wait()
        return p.stdout.read().decode('utf-8')

def get_expected(file: str) -> str:
    with open(file, "r") as f:
        return f.read()

def main(f: str):
    cur_dir = os.path.abspath(".")
    match os.path.basename(cur_dir):
        case "prev25":
            os.chdir("prg")
        case "prg": pass
        case d: raise ValueError(f"Current directory is not prev25 or prg. Is {d}")
    
    if f is not None:
        file = f
        file_name, ext = os.path.splitext(file)
        if not os.path.exists(file_name + '.out'): raise ValueError(f"{file_name}.out does not exist")
        
        expected = get_expected(file_name + '.out')
        output = run_test(file_name)
        print_diff(expected=expected, actual=output)
        return
    
    _, _, files = next(os.walk(os.curdir))
    
    for file in files:
        file_name, ext = os.path.splitext(file)
        if ext != ".prev25": continue
        if not os.path.exists(file_name + '.out'): continue
        
        print(f"START DIFF {file_name}")
        
        expected = get_expected(file_name + '.out')
        output = run_test(file_name)
        
        print_diff(expected=expected, actual=output)
        
        print("END DIFF")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(
        description="Compare all tests with their corresponding outputs. Run inside folder 'prev25' or 'prg'"
    )
    parser.add_argument("file", default=None, nargs='?', help="Name of test file")
    args = parser.parse_args()
    
    main(args.file)