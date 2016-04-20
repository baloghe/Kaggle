#!/bin/bash

echo "GIT upload start..."

git init
git add .
git commit -m 'initial version'
git remote add origin https://github.com/baloghe/Kaggle/DTA
git remote -v
git pull origin master
git push origin master
