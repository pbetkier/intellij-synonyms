#!/usr/bin/env sh

IDEA_VERSION="13.1.2"

wget -O idea.tar.gz http://download-cf.jetbrains.com/idea/ideaIC-${IDEA_VERSION}.tar.gz

tar xf idea.tar.gz
ln -sT `find . -name 'idea-IC*'`/lib idea-libs
