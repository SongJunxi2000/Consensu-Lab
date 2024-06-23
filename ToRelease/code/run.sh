#!/usr/bin/env bash
# Expected an argument Majority_Vote_Adversary or Student_Player or Dolev_Strong_FRound_Adversary
# Otherwise, all testcases will be run
java -cp classes/:lib/* Test.RunTests $1
