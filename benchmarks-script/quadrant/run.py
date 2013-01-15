import os
import re
import sys
import subprocess

cur_dir = os.path.realpath(os.path.dirname(__file__))
log_path = os.path.join(cur_dir, 'logcat_canvas.log')
result_path = os.path.join(cur_dir, 'results.txt')


def parseLog(log_file=log_path):
    if not os.path.exists(log_file):
        return
    res_f = open(log_file)
    lines = res_f.readlines()
    res_f.close()

    pat_str = (
                '^\s*D/Canvas\s*\(\s*\d+\s*\)\s*:'
                '\s*(?P<key>(Total|CPU|Mem|I/O|2D|3D))\s*:'
                '\s*(?P<score>\d+)\s*$'
                )
    pat = re.compile(pat_str)
    res_hash = {}
    for line in lines:
        match = pat.search(line)
        if not match:
            continue
        data = match.groupdict()
        res_hash[data['key'].strip()] = data['score'].strip()

    result_path = os.path.join(os.path.realpath(os.path.dirname(__file__)),
                               'results.txt')
    res_fd = open(result_path, 'w')
    for key in res_hash.keys():
        res_fd.write('%s=%s\n' % (key,
                                  res_hash.get(key)))
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
