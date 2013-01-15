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

#    D/TextView( 7764): Sieve
#    D/TextView( 7764): 8426
#    D/TextView( 7764): Loop
#    D/TextView( 7764): 17864
#    D/TextView( 7764): Logic
#    D/TextView( 7764): 11095
#    D/TextView( 7764): String
#    D/TextView( 7764): 10029
#    D/TextView( 7764): Float
#    D/TextView( 7764): 7084
#    D/TextView( 7764): Method
#    D/TextView( 7764): 5424

    key_pat_str = ('^\s*D/TextView\s*\(\s*(?P<pid>\d+)\s*\)\s*:'
               '\s*(?P<key>(Sieve|Loop|Logic|String|Float|Method))\s*$')
    key_pat = re.compile(key_pat_str)
    res_hash = {}
    for index in range(len(output)):
        line = output[index]
        match = key_pat.search(line)
        if match:
            data = match.groupdict()
            key = data['key']
            value = output[index + 1].split(':')[1].strip()
            res_hash[key] = value
            index = index + 2
        else:
            index = index + 1

    f = open(result_path, "w")
    keys = ['Sieve', 'Loop', 'Logic', 'String', 'Float', 'Method']
    for key in keys:
        value = res_hash.get(key, '0')
        f.write("CaffeineMark %s=%s\n" % (key, value))
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
