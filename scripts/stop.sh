#!/usr/bin/env bash

# 실행 중인 모든 Docker 컨테이너 ID를 가져옵니다.
container_ids=$(sudo docker ps -q)

# 컨테이너 ID가 있는 경우에만 실행합니다.
if [ ! -z "$container_ids" ]; then
  echo "실행 중인 Docker 컨테이너를 종료하고 삭제합니다."

  # 각 컨테이너를 중지하고 삭제합니다.
  sudo docker stop $container_ids
  sudo docker rm $container_ids
else
  echo "실행 중인 Docker 컨테이너가 없습니다."
fi

# 모든 Docker 이미지 ID를 가져옵니다.
image_ids=$(sudo docker images -q)

# 이미지 ID가 있는 경우에만 실행합니다.
if [ ! -z "$image_ids" ]; then
  echo "모든 Docker 이미지를 삭제합니다."

  # 각 이미지를 삭제합니다.
  sudo docker rmi -f $image_ids
else
  echo "삭제할 Docker 이미지가 없습니다."
fi