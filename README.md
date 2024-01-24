2024.1.18
git branch -D dev_wsj
git checkout dev_wsj
git branch --set-upstream-to=origin/dev_wsj
git rm -rf .
git push origin dev_wsj
git pull origin dev_wsj --allow-unrelated-histories
