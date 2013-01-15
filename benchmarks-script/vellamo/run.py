import os
import sys
import subprocess

parent_dir = os.path.realpath(os.path.dirname(__file__))
res_path = os.path.join(parent_dir, 'results.txt')


sub_items_count_hash = {
    "Total Score": 0,
    "See The Sun Canvas": 7,
    "Deep Sea Canvas": 3,
    "Aquarium Canvas": 5,
    "Pixel Blender": 5,
    "Surf Wax Binder": 5,
    "Sun Spider (Online)": 3,
    "V8 Benchmark (Online)": 3,
    "Ocean Flinger": 10,
    "Image Flinger": 10,
    "Text Flinger": 10,
    "Networking Loader": 10,
    "HTML5 Video (Online)": 2,
    "WebGL (Online)": 2,
    "Page Loader & Reloader (Online)": 3
}


def parseLog():
    if not os.path.exists(res_path):
        return

    res_f = open(res_path)
    contents = res_f.readlines()
    res_f.close()

    index = 0
    while index < len(contents):
        line = contents[index].strip()
        if line.find("' ") > -1:
            line = line.replace("' ", '.').replace('"', "'")
        contents[index] = '%s\n' % line
        for key in sub_items_count_hash.keys():
            if line.find(key) > -1:
                count = sub_items_count_hash.get(key)
                for i in range(1, count + 1):
                    contents[index + i] = "%s %s\n" % (key,
                                                  contents[index + i].strip())
                index = index + count + 1
                break

    res_f = open(res_path, 'w')
    for line in contents:
        res_f.write(line)
    res_f.close()


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
