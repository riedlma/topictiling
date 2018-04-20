import os
import sys

topic_output = sys.argv[1]
output_folder = sys.argv[2]

if not os.path.exists(output_folder):
	os.makedirs(output_folder)

out = ""
out_filename = ""
for l in open(topic_output):
	out+=l
	if l.strip()=="<document>":
		out=""
	if l.strip().startswith("<documentName>"):
		docname = l.strip().replace("<documentName>","").replace("</documentName>","")
	if l.strip().startswith("</document>"):
		fw = open(os.path.join(output_folder,docname),"w")
		fw.write(out)
		fw.close()
