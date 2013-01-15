import os
import re
import sys
import subprocess

cur_dir = os.path.realpath(os.path.dirname(__file__))
log_path = os.path.join(cur_dir, 'logcat_webview.log')
result_path = os.path.join(cur_dir, 'results.txt')


def checkResults():

    if not os.path.exists(log_path):
        return
    log_fd = open(log_path)
    lines = log_fd.readlines()
    log_fd.close()

    usefull_info = []
    begin_pat_str = ('^\s*D/WebViewClassic.loadDataWithBaseURL\(\s*\d+\s*\)'
                     '\s*:\s*(?P<content>\<.*)\s*$')
    begin_pat = re.compile(begin_pat_str)
    replace_pat = re.compile('<[^>]*>')
    found_start_of_integer_perf = False
    for line in lines:

        if not found_start_of_integer_perf:
            if line.find('overall-score') == -1 and \
               line.find('Geekbench Score') == -1:
                if line.find('<h1>Integer Performance</h1>') == -1:
                    continue
                else:
                    found_start_of_integer_perf = True

        match = begin_pat.search(line)
        if not match:
            continue
        data = match.groupdict()
        value = data['content'].strip()
        if value.find('<h1>') > -1:
            continue
        value = re.sub(replace_pat, '', value)
        if not value.strip():
            continue
        usefull_info.append(value)

    res_ary = []
    index = 0
    while index < len(usefull_info):
        line = usefull_info[index].strip()
        if line.find("Score") > -1:
            res_ary.append({'key': line,
                            'value': usefull_info[index + 1].strip()})
            index = index + 2
        else:
            key = "%s_%s" % (line, usefull_info[index + 1].strip())
            res_ary.append({'key': "%s Score" % key,
                            'value': usefull_info[index + 2].strip()})
            res_ary.append({'key': key,
                            'value': usefull_info[index + 3].strip()})
            index = index + 4

    result_path = os.path.join(os.path.realpath(os.path.dirname(__file__)),
                               'results.txt')
    res_fd = open(result_path, 'w')
    for test_hash in res_ary:
        res_fd.write('%s=%s\n' % (test_hash.get('key'),
                                  test_hash.get('value')))
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
        checkResults()

if __name__ == '__main__':
    main()
