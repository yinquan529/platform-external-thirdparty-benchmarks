import os
import sys
import subprocess
import xml.dom.minidom

parent_dir = os.path.realpath(os.path.dirname(__file__))
res_path = os.path.join(parent_dir, 'results.txt')
result_f = os.path.join(parent_dir, 'last_results_2.5.1.xml')


def getText(node):
    children = node.childNodes
    rc = []
    for node in children:
        if node.nodeType == node.TEXT_NODE:
            rc.append(node.data)
    return ''.join(rc)


def parseLog(res_file=result_f):
    if not res_file:
        return []
    if not os.path.exists(res_file):
        print "File(%s) does not exist" % res_file
        return []

    result_list = []
    try:
        dom = xml.dom.minidom.parse(res_file)
        results = dom.getElementsByTagName('test_result')
        for test in results:
            title = getText(test.getElementsByTagName('title')[0])
            test_type = getText(test.getElementsByTagName('type')[0])
            score = getText(test.getElementsByTagName('score')[0])
            fps = getText(test.getElementsByTagName('fps')[0])
            uom = getText(test.getElementsByTagName('uom')[0])
            test_case_id = "%s_%s" % (title, test_type)
            result_list.append("%s=%s %s" % (test_case_id, score, uom))
            if fps:
                result_list.append("%s_fps=%s" % (test_case_id, fps))
    except Exception, e:
        print "Has exception to parse the xml filei: %s" % res_file
        print "Exception: %s" % e

    res_fd = open(res_path, 'w')
    for line in result_list:
        res_fd.write('%s\n' % line)
    res_fd.close()


def main():
    dev_ids = []
    if len(sys.argv) >= 2:
        dev_ids = sys.argv[1:]
    else:
        dev_ids = ['']
    for dev_id in dev_ids:
        if os.path.exists(res_path):
            os.unlink(res_path)
        run_sh = os.path.realpath(os.path.dirname(__file__)) + "/run.sh"
        subprocess.call(['/bin/bash', run_sh, dev_id])
        parseLog()


if __name__ == '__main__':
    main()
