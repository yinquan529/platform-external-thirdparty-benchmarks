import os
import sys
import subprocess

cur_dir = os.path.realpath(os.path.dirname(__file__))
log_path = os.path.join(cur_dir, 'logcat.log')
result_path = os.path.join(cur_dir, 'results.txt')


def checkResults():
    if not os.path.exists(log_path):
        return
    log_fd = open(log_path)
    output = log_fd.readlines()
    log_fd.close()

    linpack_st = 0
    linpack_mt = 0
    for index in range(len(output)):
        line = output[index]
        if line.find("D/TextView(") == -1:
            continue
        if (line.find('Inaccurate Result') > 0):
            words = output[index - 3].split()
            linpack_st = words[len(words) - 1].strip()

        if (line.find('Inconsistent Result') > 0):
            words = output[index - 3].split()
            linpack_mt = words[len(words) - 1].strip()
            break

    f = open(result_path, "w")
    f.write("LinPack_ST=" + str(linpack_st) + "\n")
    f.write("LinPack_MT=" + str(linpack_mt) + "\n")
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
