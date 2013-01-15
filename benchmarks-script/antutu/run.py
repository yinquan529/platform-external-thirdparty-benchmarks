import os
import re
import subprocess
import sys

cur_dir = os.path.realpath(os.path.dirname(__file__))
log_path = os.path.join(cur_dir, 'logcat.log')
log_path_canvas = os.path.join(cur_dir, 'logcat_canvas.log')
result_path = os.path.join(cur_dir, 'results.txt')


def getResultFromCanvas():
    if not os.path.exists(log_path_canvas):
        return
    log_fd = open(log_path_canvas)
    output = log_fd.readlines()
    log_fd.close()
    #D/Canvas  ( 6368): RAM:
    #D/Canvas  ( 6368): 727
    #D/Canvas  ( 6368): CPU integer:
    #D/Canvas  ( 6368): 1291
    #D/Canvas  ( 6368): CPU float-point:
    #D/Canvas  ( 6368): 1049
    #D/Canvas  ( 6368): 2D graphics:
    #D/Canvas  ( 6368): 293
    #D/Canvas  ( 6368): 3D graphics:
    #D/Canvas  ( 6368): 1212
    #D/Canvas  ( 6368): Database IO:
    #D/Canvas  ( 6368): 330
    #D/Canvas  ( 6368): SD card write:
    #D/Canvas  ( 6368): 0
    #D/Canvas  ( 6368): SD card read:
    #D/Canvas  ( 6368): 0
    tmp_hash = {'RAM': 'memory',
              'CPU integer': 'integer',
              'CPU float-point': 'float',
              '2D graphics': 'score2d',
              '3D graphics': 'score3d',
              'Database IO': 'database',
              'SD card write': 'sdwrite',
              'SD card read': 'sdread',
              'Total score': 'score'
             }
    res_hash = {}
    for index in range(len(output)):
        tmp_ary = output[index].split(':')
        if len(tmp_ary) != 3:
            index = index + 1
            continue
        key = tmp_ary[1].strip()
        if key in tmp_hash.keys():
            value = output[index + 1].split(':')[1].strip()
            res_hash[tmp_hash[key]] = value
            index = index + 2
    return res_hash


def checkResults():
    if not os.path.exists(log_path):
        return
    log_fd = open(log_path)
    output = log_fd.readlines()
    log_fd.close()

    #I/Antutu Benchmark( 3373): memory:     789
    #I/Antutu Benchmark( 3373): integer:     1319
    #I/Antutu Benchmark( 3373): float:     1084
    #I/Antutu Benchmark( 3373): score2d:     332
    #I/Antutu Benchmark( 3373): score3d:     1313
    #I/Antutu Benchmark( 3373): database:     260
    #I/Antutu Benchmark( 3373): sdwrite:     0
    #I/Antutu Benchmark( 3373): sdread:     0
    #I/Antutu Benchmark( 3373): score:     5097
    pat_str = ('^\s*I/Antutu Benchmark\s*\(\s*\d+\s*\)\s*:'
               '\s*(?P<key>(memory|integer|float|score2d|score3d|'
               'database|sdwrite|sdread|score))\s*:'
               '\s*(?P<score>\d+)\s*$'
                )
    pat = re.compile(pat_str)
    res_hash = {}
    for line in output:
        match = pat.search(line)
        if not match:
            continue
        data = match.groupdict()
        res_hash[data['key'].strip()] = data['score'].strip()

    if len(res_hash) == 0:
        res_hash = getResultFromCanvas()

    tmp_hash = {'integer': 'Antutu CPU integer',
                'float': 'Antutu CPU float-point',
                'memory': 'Antutu RAM',
                'score2d': 'Antutu 2D graphics',
                'score3d': 'Antutu 3D graphics',
                'database': 'Antutu Database IO',
                'sdwrite': 'Antutu SD card write',
                'sdread': 'Antutu SD card read',
                'score': 'Antutu Total'
               }
    f = open(result_path, "w")
    for key in res_hash.keys():
        f.write('%s=%s\n' % (tmp_hash[key], res_hash.get(key)))
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
