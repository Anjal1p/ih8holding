
# coding: utf-8

# In[4]:

import glob
import PreprocessingFunction as pf
keyword={}
for filename in glob.glob('../DataLookupSystem/data/*'):
    print(filename)
    if filename== '../DataLookupSystem/data/blacklist':
        continue
    else:
        data = open(filename).read().strip().split('\n')
        for d in data:
#             print(d)
            num = int(d.split('\t')[0])
            word = d.split('\t')[1]
            pfword = pf.preprocessing(word)[1]
            if len(pfword)!=1:
#                 print(pfword)
                continue
            else:
                if pfword[0] not in keyword:
                    keyword[pfword[0]]=num
                else:
                    keyword[pfword[0]]+=num



# In[5]:

sorted_by_value = sorted(keyword.items(), key=lambda kv: kv[1], reverse=True)


# In[6]:

file = '../keywords/keywords_all_normalized.txt'
fp = open(file, "a")
for s in sorted_by_value:
    str_ = str(s[1])+'\t'+s[0]+'\n'
#     print(str_)
    fp.write(str_)
fp.close()


# In[ ]:



