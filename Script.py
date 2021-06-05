from urllib.request import urlopen
from bs4 import BeautifulSoup
import random

def Pi( pattern):                  #Building the pi table by  matching the pattern with itself.
    pi = [0]
    for q in range(1, len(pattern)):
        k = pi[q - 1]
        while k > 0 and pattern[k] != pattern[q]:
            k = pi[k - 1]
        if(pattern[k] == pattern[q]):
            pi.append(k+1)
        else:
            pi.append(k)
    return pi
        
def search( T, P):
    PI=Pi(P)                           #Preprocessing
    ret=[]
    q = 0                                   #Initial State, 0 correct characters so far.
    for i in range(len(T)):                 #Loop on the text
        while q > 0 and T[i] != P[q]:
            q = PI[q - 1]                   #Mistmatch --> Relocate
        if T[i] == P[q]: q += 1             #Match --> Advance the state
        if q == len(P):                     #If the pattern is found mark the spot then relocate (so we can try to reproduce it perhaps)
            ret.append(i - (q - 1))
            q = PI[q - 1]
    return ret


def seekSpace(Stringo,Start,farEnd):        #find the a decent period to the left and a decent space on the right.
    if(farEnd-Start)>=0:
        for i in range(farEnd,Start,-1):
            if(Stringo[i]==" "):
                break;
        return i
    else:
         j=0
         for i in range(farEnd,Start):
            if(Stringo[i]=="."):
                j=i
                break;
         if(j==Start):
            for i in range(farEnd,Start):
                if(Stringo[i]==" "):
                    break;
    return i+1


def SoupTime(url):
    url = "https://en.wikipedia.org/wiki/Fibonacci_heap"
    html = urlopen(url).read()
    soup = BeautifulSoup(html, features="html.parser")

    for script in soup(["script", "style"]):            #Killing JS elements
        script.extract()    
    text = soup.get_text()                              #Now we can get the text.

    lines = (line.strip() for line in text.splitlines())                        #splitting at \n and removing all trailing and ending spaces on each (Strip).
    chunks = (phrase.strip() for line in lines for phrase in line.split("  "))  #Removing more useless spaces.
    # drop blank lines and join it all again.
    text = '\n'.join(chunk for chunk in chunks if chunk)

    return text

def Grasp(URL,Word,Q,l,r):          #Q=0 if we don't want a random instance of the word, l and r for how many characters to travel left and right before displaying the text.

    text=SoupTime(URL)                  #Get the text within.
    A=search(text, Word)
    i=0
    while(len(A)==0 and i<4):           #If we can't find it, then perhaps we can find its origin (Stemming)
        A=search(text, Word[0:len(Word)-i])
        i=i-1;

    if(len(A)==0):
        A=search(text,"The")            #Incase the website is a mistake.


    if(Q!=0):                           #Just pick any of the instances if the hyperparameter is unspecified.
        Q=random.choice(A)
        St=seekSpace(text,Q,Q-l)      #Start after going left for l characters.
    else:
        Q=A[0]
        St=Q-1
    En=seekSpace(text,Q,Q+r)          #start after going right for r characters
    Res=text[St:Q-1]+"\033[1m" + text[Q-1:Q+len(Word)] + "\033[0m"+text[Q+len(Word):En]
    Res=Res.replace("\n"," ")

    return(Res)

print(Grasp("https://en.wikipedia.org/wiki/Fibonacci_heap","heap",0,0,200))

