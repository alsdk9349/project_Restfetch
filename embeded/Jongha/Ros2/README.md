# 1. 2024-08-26 ~ 2024-08-30 : Ros2 개인 학습 및 테스트

WSL 설치 및 설정 후 Gazebo 환경을 구축하려 했으나, Gazebo의 GUI 화면이 표시되지 않음.

## Gazebo 가상환경 구축 트러블 슈팅 :
- ~~다시 시도 및 VcXsrv 설치~~
- ~~Windows 업데이트~~
- ~~관리자 권한 실행 및 재부팅 주기화~~
- ~~WSL, Ubuntu 삭제 후 재설치~~
- 이전 시도까지 블로그 및 ChatGPT를 참고했으나, WSL이 Microsoft에서 제공하는 도구이므로, Microsoft 공식 페이지를 참고하여 문제 해결을 진행함.  
(자세한 내용은 학습 참고 자료 중 [WSL 설치 및 설정], [Linux GUI 앱 실행], [WSLg로 "디스플레이를 열 수 없음" 문제] 등 참고)

## Gazebo 가상환경 구축 문제 원인 추정 :
- **Windows 버전** (Windows 11 또는 Windows 10 버전 빌드 19044 이상)
    - 본인 문제는 아니었으나 WSLg의 X 서버 연결은 빌드 21364+ 라고 되어있지만 19045로 해결됨.
- ~~**vGPU용 드라이버**~~
    - 설치 없이 해결됨.
- **VcXsrv 설치 및 설정**
    - 초기 설정에서는 설치 없이 진행했으나, X서버 사용을 위해 필요함.
    - 3번째 설정 이후 Display 설정이 -1로 잘못 설정된 것을 확인하고 0으로 수정함.
- **Window 관리자 권한 및 Ubuntu Root 권한**
    - 권한 문제로 인해 중간에 패키지 설치가 안 되는 경우가 있음.
- **Ubuntu 버전**
    - 초기에 Ubuntu-20.04 를 사용했으나, 마지막에 Ubuntu-22.04를 설치 후 문제 해결됨.
    - Ubuntu-20.04로 Gazebo GUI를 사용하는 동기생이 있는 걸로 보아 Ubuntu 버전 문제는 아닐 듯 함.
- **systemd 에서 작동**
    - 다음 내용을 `/etc/tmpfiles.d/wslg.conf`에 추가한 후 재시작
    ```bash
    /etc/tmpfiles.d/wslg.conf
    ```

    ```conf
    #  This file is part of the debianisation of systemd.
    #
    #  systemd is free software; you can redistribute it and/or modify it
    #  under the terms of the GNU General Public License as published by
    #  the Free Software Foundation; either version 2 of the License, or
    #  (at your option) any later version.

    # See tmpfiles.d(5) for details

    # Type Path           Mode UID  GID  Age Argument
    L+     /tmp/.X11-unix -    -    -    -   /mnt/wslg/.X11-unix
    ```

    - 이 방법이 가장 유력한 해결책으로 보임.
    ([WSLg로 "디스플레이를 열 수 없음" 문제]에서 "/etc/fstab에서 /tmp 설정" 목차 참고)

## Ros2 설치 및 초기 설정 :
- Youtube 강의는 Ros2 foxy 버전을 사용했지만, 설치 시 E: Unable to locate package ros-foxy-desktop 에러로 Ros2 humble(LTS) 버전을 설치함.
    - 해당 에러는 ubuntu-22.04 에서 ros-foxy를 지원하지 않아서 생긴 에러, ubuntu-20.04에서 다시 진행해보니 ros-foxy로 설치가 됨.
- Ros2 Humble(LTS) 설치는 하단의 [Ros2 설치] 참고

## 학습 참고 자료 : 
- [WSL 설치 및 설정](https://learn.microsoft.com/ko-kr/windows/wsl/install)
- [Linux GUI 앱 실행](https://learn.microsoft.com/ko-kr/windows/wsl/tutorials/gui-apps)
- [WSLg로 "디스플레이를 열 수 없음" 문제](https://github.com/microsoft/wslg/wiki/Diagnosing-%22cannot-open-display%22-type-issues-with-WSLg)
- [VcXsrv 설치](https://sourceforge.net/projects/vcxsrv/)
- [Ros2 설치](https://docs.ros.org/en/humble/Installation/Alternatives.html)
- [ROS2 YouTube 강의](https://www.youtube.com/watch?v=X9uYIumhU8E&t=1s)
- [Ros2 notion 자료](https://puzzling-cashew-c4c.notion.site/ROS-2-for-G-Camp-6f86b29e997e445badb69cc0af825a71)