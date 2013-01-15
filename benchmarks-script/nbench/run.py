import os
import re
import sys
import subprocess

cur_dir = os.path.realpath(os.path.dirname(__file__))
log_path = os.path.join(cur_dir, 'logcat_textview.log')
result_path = os.path.join(cur_dir, 'results.txt')

def parseLog(log_file=log_path):
    test_ids = ['NUMERIC SORT',
                'STRING SORT',
                'BITFIELD',
                'FP EMULATION',
                'FOURIER',
                'ASSIGNMENT',
                'IDEA',
                'HUFFMAN',
                'NEURAL NET',
                'LU DECOMPOSITION']
    results = {}
    if not os.path.exists(log_file):
        return

    log_fd = open(log_file)
    for line in log_fd.readlines():
        for test_id in test_ids:
            if line.find(test_id) >= 0:
                elements = line.split(':')
                results[test_id] = elements[2].strip()
    log_fd.close()

    res_fd = open(result_path, "w")
    for test_id in results.keys():
        res_fd.write('%s=%s\n' % (test_id, results[test_id]))
    res_fd.close()


def main():
    dev_ids = []
    if len(sys.argv) >= 2:
        dev_ids = sys.argv[1:]
    else:
        dev_ids = ['']
    for dev_id in dev_ids:
        if os.path.exists(result_path):
            os.unlink(result_path)
        run_sh = os.path.realpath(os.path.dirname(__file__)) + "/run.sh"
        subprocess.call(['/bin/bash', run_sh, dev_id])
        parseLog()


if __name__ == '__main__':
    main()
