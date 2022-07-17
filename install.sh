starter_name=starter-kotlin-maven
repo=https://github.com/alfu32/$starter_name.git
rand_tag=`echo $RANDOM | md5sum | head -c 20`

echo $rand_tag


git clone $repo "$starter_name-$rand_tag"

cd "$starter_name-$rand_tag"

rm -rf .git

git init

