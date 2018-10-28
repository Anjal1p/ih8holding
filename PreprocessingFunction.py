
# coding: utf-8

# In[24]:

def preprocessing(sentence):
    import nltk
    from nltk.corpus import stopwords
    from nltk.stem import PorterStemmer
    from nltk.stem.wordnet import WordNetLemmatizer
    import re

    num_dic={"one":1, "two":2, "three":3, "four":4, "five":5, "six":6, "seven":7, "eight":8, "nine":9, "ten":10, "zero":0}
    stop_list = set(stopwords.words('english'))
    tokens = nltk.word_tokenize(sentence.lower())
    symbol = []
    num = []
    can_keyword = []
    can_keyword_dic={}
    ps = PorterStemmer()
    for t in tokens:
        if t not in stop_list:
            if re.search(r"[1-9]", t):
                num.append(t)
            elif t in num_dic:
                num.append(t)
            elif re.search(r"\W", t):
                symbol.append(t)
            else:
                nor_word = WordNetLemmatizer().lemmatize(t,'v')
                nor_word_2 = ps.stem(nor_word)
                can_keyword.append(nor_word_2)
                if nor_word_2 not in can_keyword_dic:
                    can_keyword_dic[nor_word_2] = 1
                else:
                    can_keyword_dic[nor_word_2]+=1
    sorted_by_value = sorted(can_keyword_dic.items(), key=lambda kv: kv[1], reverse=True)
    return(sorted_by_value, can_keyword)


def main():
    sentence = "I refilled my T-Mobile prepaid (pay as you go - legacy) phone.  I added $10 to my phone account which gives me an additional 35 minutes added which will expire in one year for gold members.  I used to only pay the NYS tax (8.875%), therefore on a $10 refill, you would think the total with tax should've been $10.89.  Something changed, I was charged an additional $1.20, making the tax amount $2.09.  The total refill amount paid was $12.09.  I had to find out why."
    print(preprocessing(sentence))


if __name__ == "__main__":
    main()

