#!/usr/bin/env python
# -*- coding: utf-8 -*-

import random

OUTPUT_FILE = '/tmp/genome.txt'
BP_CHARS = ('A', 'T', 'G', 'C')
MAX_LENGTH = 3 * 100
PROFILE_NUM = 4

if __name__ == "__main__":
    f = open(OUTPUT_FILE, 'w')
    bp_length = len(BP_CHARS)

    for profle_n in range(0, PROFILE_NUM):
        for i in range(0, MAX_LENGTH):
            n = random.randint(0, bp_length - 1)
            c = BP_CHARS[n]
            # print(str(n) + ' ' + c)
            f.write(c)
        f.write("\n")

    f.close()
    print('Done.')
    print('Upload generated file: %s to HDFS.' % OUTPUT_FILE)
