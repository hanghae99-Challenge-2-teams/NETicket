#!/usr/bin/env bash

# 새로운 이미지 이름과 태그를 설정합니다.
user_name="yujin17"
image_name="neticket-image"
image_tag="latest"

# 이미지를 다운로드합니다.
echo "새로운 Docker 이미지를 다운로드합니다."
sudo docker pull $user_name/$image_name:$image_tag

# 이미지를 실행합니다.
echo "새로운 Docker 컨테이너를 실행합니다."
port_mapping="8080:8080"
sudo docker run -d -p $port_mapping $user_name/$image_name:$image_tag