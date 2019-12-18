FROM ubuntu:18.04

#RUN apt-get install -y apt-utils
RUN apt-get update \
  && apt-get install -y xmlstarlet=1.6.1-2 \
    octave=4.2.2-1ubuntu1 \
    git=1:2.17.1-1ubuntu0.5 \
    python2.7 \
    vim=2:8.0.1453-1ubuntu1.1 \
    gawk=1:4.1.4+dfsg-1build1 \
    fig2dev=1:3.2.6a-6ubuntu1 \
  && rm -rf /var/lib/apt/lists/* \
  && ln -s /usr/bin/python2.7 /usr/bin/python \
  && git clone https://github.com/filipius/colluding.git \
  && git clone https://github.com/derekbruening/bargraph.git \
  && ln -s /bargraph/bargraph.pl /colluding/Analysis/gnuplot/bargraph.pl

ENV PYTHONPATH /colluding/Analysis/src
