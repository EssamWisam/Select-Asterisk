from urllib.request import urlopen
from bs4 import BeautifulSoup
import random
import sys


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

def Grasp(URL):         

    text=SoupTime(URL)               
    Res=text[0:100]
    Res=Res.replace("\n"," ")

    return(Res)


print(Grasp(sys.argv[1]))
