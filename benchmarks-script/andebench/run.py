import os
import re
import sys
import subprocess

cur_dir = os.path.realpath(os.path.dirname(__file__))
log_path = os.path.join(cur_dir, 'logcat_textview.log')
result_path = os.path.join(cur_dir, 'results.txt')


def checkResults():
    if not os.path.exists(log_path):
        return
    log_fd = open(log_path)
    output = log_fd.readlines()
    log_fd.close()

#    D/TextView( 4338): AndEMark Native: 4327
#    D/TextView( 4338): AndEMark Java: 181
    pat_str = ('^\s*D/TextView\s*\(\s*\d+\s*\)\s*:'
               '\s*(?P<key>(AndEMark Native|AndEMark Java))\s*:'
               '\s*(?P<value>\d+)\s*$')
    pat = re.compile(pat_str)
    res_hash = {}
    for line in output:
        match = pat.search(line)
        if match:
            data = match.groupdict()
            res_hash[data['key']] = data['value']

    f = open(result_path, "w")
    for key, value in res_hash.items():
        f.write("%s=%s\n" % (key, value))
    f.close()


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
        checkResults()


if __name__ == '__main__':
    main()
