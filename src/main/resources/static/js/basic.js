// 회원가입 페이지

$(document).ready(function () {
  // 회원가입 버튼 클릭 시
  $("#signup-btn").click(function () {
    let email = $("#email").val();
    let password = $("#password").val();
    let passwordCheck = $("#passwordCheck").val();
    let nickname = $("#nickname").val();
    // let isAdmin = $("#disabledFieldsetCheck").is(":checked");

    // 비밀번호와 비밀번호 확인 값이 일치하지 않을 경우
    if (password !== passwordCheck) {
      alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
      return;
    }

    // 회원가입 요청
    $.ajax({
      type: "POST",
      url: "/api/neticket/signup",
      contentType: "application/json",
      data: JSON.stringify({
        email: email,
        password: password,
        nickname: nickname,
        // isAdmin: isAdmin
      }),
      success: function (data) {
        alert("회원가입이 완료되었습니다.");
        window.location.href = "/neticket/login";
      },
      error: function (jqXHR, textStatus, errorThrown) {
        alert("회원가입에 실패했습니다.")
      }
    });
  });

  // 비밀번호 입력값 사용자가 안보이게 가려주기
  $("#password").focus(function () {
    $("#password").attr("type", "password");
  });

  $("#password").blur(function () {
    if ($("#password").val() === "") {
      $("#password").attr("type", "text");
    }
  });

  $("#passwordCheck").focus(function () {
    $("#passwordCheck").attr("type", "password");
  });

  $("#passwordCheck").blur(function () {
    if ($("#passwordCheck").val() === "") {
      $("#passwordCheck").attr("type", "text");
    }
  });
});

// 로그인 페이지

function login() {
  let useremail = $('#login_email').val();
  let password = $('#login_password').val();

  if (useremail == '') {
    alert('ID를 입력해주세요');
    return;
  } else if (password == '') {
    alert('비밀번호를 입력해주세요');
    return;
  }

  $.ajax({
    type: "POST",
    url: '/api/neticket/login',
    contentType: "application/json",
    data: JSON.stringify({
      email: useremail,
      password: password
    }),
    success: function (response, status, xhr) {
      alert("로그인 성공!")
      // Authorization 헤더 값을 쿠키에 저장합니다.
      document.cookie = 'Authorization=' + xhr.getResponseHeader(
          'Authorization') + ';path=/';
      // 로그인 성공 시, 이동할 페이지로 리다이렉트합니다.
      window.location.href = "/neticket";
    },
    error: function (jqXHR, textStatus, errorThrown) {
      // 요청이 실패한 경우, 처리할 코드를 작성합니다.
      alert("로그인에 실패하였습니다.");
    }
  })
}

// 메인 페이지
// $(document).ready(function () {
//   showEvent(1);
//
//   $(document).on('click', '.col', function () {
//     let eventIdString = $(this).data('event-id');
//     let eventId = parseInt(eventIdString, 10);
//     window.location.href = "/neticket/events/" + eventId;
//   });
//
// });

// 행사정보 조회
function showEvent(pageNum) {
  $.ajax({
    type: "GET",
    url: "/api/neticket/events?page=" + pageNum,
    dataType: "json",
    headers: {'Content-Type': 'application/json'},
    success: function (response) {
      showPaging(response);

      $('#event_box').empty();
      for (let i = 0; i < response.content.length; i++) {
        let eventDto = response.content[i];
        let id = eventDto.id;
        let image = eventDto.image;
        let title = eventDto.title;
        let date = eventDto.date;
        let place = eventDto.place;

        let tmpevent = `<div class="col" data-event-id="${id}">
                          <div class="card h-100">
                            <img src="https://neticketbucket.s3.ap-northeast-2.amazonaws.com/uploaded-image/${image}"
                                 class="card-img-top" alt="...">
                            <div class="card-body">
                              <h5 class="card-title">${title}</h5>
                              <h6 class="card-date">${date}</h6>
                              <p class="card-text">${place}</p>
                            </div>
                          </div>
                        </div>`
        $('#event_box').append(tmpevent)
      }
    },
  })
}

// 페이징 처리
function showPaging(response) {
  let totalPages = response.totalPages;
  let currentPage = response.number + 1;
  let pagination = $('.pagination');

  pagination.empty(); // 이 부분을 추가
  for (let i = 1; i <= totalPages; i++) {
    let pageItem = `<li class="page-item ${(i === currentPage) ? 'active'
        : ''}"><a class="page-link" onclick="showEvent(${i})" href="#">${i}</a></li>`;
    pagination.append(pageItem);
  }
}

// 상세 페이지
// $(document).ready(function () {
//   // 이벤트 ID를 URL에서 가져옵니다.
//   const eventId = window.location.pathname.split('/').pop();
//
//   // 이벤트 세부 정보를 가져옵니다.
//   getEventDetails(eventId);
//
//   // 1초마다 남은 시간을 갱신하는 타이머를 시작합니다.
//   setInterval(updateRemainingTime, 1000);
// });

let isAvailable;

function getEventDetails(eventId) {
  $.ajax({
    type: "GET",
    url: "/api/neticket/events/" + eventId,
    dataType: "json",
    success: function (event) {
      const imageUrl = 'https://neticketbucket.s3.ap-northeast-2.amazonaws.com/uploaded-image/'
          + event.image;
      $('.image').attr('src', imageUrl);
      $('.title').text(event.title);
      $('.place').text(event.place);
      $('.price').text(addCommas(event.price) + '원');
      $('.date').text(event.date);
      isAvailable = event.ticketInfoDto.available;
      $('.isAvailable').text(isAvailable ? '예매 가능' : '예매 불가능');
      $('.remainingTime1').text(event.ticketInfoDto.openDate);
      // 이벤트의 티켓 정보를 저장합니다.
      $('#bookingButton').data('ticketInfo', event.ticketInfoDto);
      // 남은 시간을 표시합니다.
      updateRemainingTime();
    },
    error: function (jqXHR, textStatus, errorThrown) {
      console.error("AJAX request failed: " + textStatus + ", " + errorThrown);
    }
  });
}

function addCommas(number) {
  return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function updateRemainingTime() {
  // 예매 버튼에 저장된 티켓 정보를 가져옵니다.
  const ticketInfo = $('#bookingButton').data('ticketInfo');
  // 티켓 정보가 없으면 함수를 종료합니다.
  if (!ticketInfo) {
    return;
  }
  // 티켓 오픈 시간을 가져옵니다.
  const openTime = new Date(ticketInfo.openDate);
  // 현재 시간을 가져옵니다.
  const now = new Date();
  // 남은 시간을 계산합니다.
  const remainingTime = openTime - now;
  // 남은 시간이 음수이면 예매가 종료된 것으로 판단하고, 버튼을 활성화합니다.
  if (remainingTime < 0) {
    $('#bookingButton').prop('disabled', false).text('예매하기');
    $('.remainingTime').text('');
    return;
  }
  // 시간, 분, 초를 계산합니다.
  const hours = Math.floor(remainingTime / (1000 * 60 * 60));
  const minutes = Math.floor((remainingTime % (1000 * 60 * 60)) / (1000 * 60));
  const seconds = Math.floor((remainingTime % (1000 * 60)) / 1000);
  // 타이머를 업데이트합니다.
  const remainingTimeStr = `${hours.toString().padStart(2,
      '0')}:${minutes.toString().padStart(2,
      '0')}:${seconds.toString().padStart(2, '0')}`;
  $('.remainingTime').text(remainingTimeStr);
}

function onBookingButtonClick() {
  // 예매 버튼에 저장된 티켓 정보를 가져옵니다.
  const ticketInfo = $('#bookingButton').data('ticketInfo');
  // 티켓 정보가 없으면 함수를 종료합니다.
  if (!ticketInfo) {
    return;
  }
  if (!isAvailable) {
    alert('예매가 불가능한 공연입니다.');
    return;
  }
  // 티켓 정보 ID를 가져옵니다.
  const ticketInfoId = BigInt(ticketInfo.ticketInfoId);

  // 예매 중 페이지 URL을 생성합니다.
  const url = `/neticket/reservations/in-progress/` + ticketInfoId;
  // 페이지로 이동합니다.
  window.location.href = url;
}

// 예약 중 페이지
// $(document).ready(function () {
//   getEventInfo();
// });

function getEventInfo() {

  const pathArray = window.location.pathname.split('/');
  const ticketInfoId = pathArray[pathArray.length - 1];
  console.log(ticketInfoId);

  $.ajax({
    type: "GET",
    url: "/api/neticket/ticket-info/" + ticketInfoId,
    dataType: "json",
    success: function (event) {
      $('.title').text(event.title);
      $('.place').text(event.place);
      $('.price').text(addCommas(event.price) + '원');
      $('.date').text(event.date);
    },
    error: function (jqXHR, textStatus, errorThrown) {
      console.error("AJAX request failed: " + textStatus + ", " + errorThrown);
    }
  });
}

function addCommas(number) {
  return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function getAuthTokenFromCookie() {
  return document.cookie.split(';').find(
          cookie => cookie.trim().startsWith('Authorization='))?.split('=')[1]
      || null;
}

function saveReservation() {

  let token = getAuthTokenFromCookie(); // 쿠키에서 토큰 값 추출
  console.log(token)

  // select 요소를 찾습니다.
  let selectElement = document.getElementById('ticketCount');

  // 선택된 option 요소의 value 값을 가져옵니다.
  let selectedValue = selectElement.value;

  const pathArray = window.location.pathname.split('/');
  const ticketInfoId = pathArray[pathArray.length - 1];
  console.log(ticketInfoId);

  $.ajax({
    type: "POST",
    url: "/api/neticket/reservations",
    dataType: "json",
    headers: {
      'Content-Type': 'application/json',
      'Authorization': token // Authorization 헤더에 토큰 값 추가
    },
    data: JSON.stringify({
      ticketInfoId: ticketInfoId,
      count: selectedValue
    }),
    success: function (resvId) {
      alert("예매가 완료되었습니다.");
      window.location.href = "/neticket/reservations/completed/" + resvId;
    },
    error: function (jqXHR, textStatus, errorThrown) {
      // 요청이 실패한 경우, 처리할 코드를 작성합니다.
      alert("요청이 실패하였습니다.");
    }
  })
}

// 예약 완료 페이지
// $(document).ready(function () {
//   showReservationCompleted();
// });

function getAuthTokenFromCookie() {
  return document.cookie.split(';').find(
          cookie => cookie.trim().startsWith('Authorization='))?.split('=')[1]
      || null;
}

function showReservationCompleted() {

  let token = getAuthTokenFromCookie(); // 쿠키에서 토큰 값 추출
  console.log(token)

  const pathArray = window.location.pathname.split('/');
  const resvId = pathArray[pathArray.length - 1];
  console.log(resvId);

  $.ajax({
    type: "GET",
    url: "/api/neticket/reservations/" + resvId,
    dataType: "json",
    headers: {
      'Content-Type': 'application/json',
      'Authorization': token // Authorization 헤더에 토큰 값 추가
    },
    success: function (response) {
      console.log(response)
      $('#getresv').empty(); // 이 부분을 추가
      let id = response.id;
      let title = response.title;
      let place = response.place;
      let date = response.date;
      let totalPrice = response.totalPrice;
      let count = response.count;

      let temp = `<h5 class="card-header">예매완료</h5>
                    <div class="card-body" id="getresv">
                    <ul class="info">
                      <li class="infoItem"><strong class="infoLabel">예매 번호 : </strong>
                        <div class="infoDesc" id="resvId">${id}</div>
                      </li>
                      <li class="infoItem"><strong class="infoLabel">공연 제목 : </strong>
                        <div class="infoDesc">${title}</div>
                      </li>
                      <li class="infoItem"><strong class="infoLabel">공연 장소 : </strong>
                        <div class="infoDesc"><p class="infoText">${place}</p></div>
                      </li>
                      <li class="infoItem infoDate"><strong class="infoLabel">공연 날짜 : </strong>
                        <div class="infoDesc"><p class="infoText">${date}</p></div>
                      </li>
                      <li class="infoItem infoPrice"><strong class="infoLabel">가격 : </strong>
                        <div class="infoDesc"><p class="infoText">${totalPrice}원</p></div>
                      </li>
                      <li class="infoItem"><strong class="infoLabel">매수 : </strong>
                        <div class="infoDesc"><p class="infoText">${count}매</p></div>
                      </li>
                    </ul>
                  </div>`
      $('#getresv').append(temp)
    },
  })
}


// addevent
function addevent() {
  $("#eventForm").on("submit", function (event) {
    event.preventDefault();

    let formData = new FormData(this);
    let eventRequestDto = {
      title: $("#title").val(),
      place: $("#place").val(),
      price: $("#price").val(),
      date: $("#date").val() + "T00:00:00",
      openDate: $("#openDate").val() + "T00:00:00",
      totalSeat: $("#totalSeat").val()
    };

    const json = JSON.stringify(eventRequestDto);
    const blob = new Blob([json], {type: "application/json"});
    formData.append("dto", blob);

    // Check if the image input has a file
    let imageFile = $('#image')[0].files[0];
    if (imageFile) {
      formData.append("image", imageFile);
    }
    let token = getAuthTokenFromCookie(); // 쿠키에서 토큰 값 추출

    // AJAX 요청을 수행합니다.
    $.ajax({
      url: "/api/neticket/events",
      type: "POST",
      enctype: 'multipart/form-data',
      data: formData,
      headers: {
        'Authorization': token // Authorization 헤더에 토큰 값 추가
      },
      processData: false,
      contentType: false,
      success: function (response) {
        if (response.statusCode === 201) {
          alert(response.msg);
        } else {
          alert("오류가 발생했습니다. 다시 시도해주세요.");
        }
      },
      error: function (jqXHR, textStatus, errorThrown) {
        // 오류 발생 시 처리할 내용을 작성합니다.
        alert("오류가 발생했습니다. 다시 시도해주세요.");
      }
    });
  });
}

// 마이페이지
function showMyPage() {

  let token = getAuthTokenFromCookie(); // 쿠키에서 토큰 값 추출
  console.log(token)

  $.ajax({
    type: "GET",
    url: "/api/neticket/user",
    dataType: "json",
    headers: {
      'Content-Type': 'application/json',
      'Authorization': token // Authorization 헤더에 토큰 값 추가
    },
    success: function (response) {
      console.log(response)
      $('#getresv').empty(); // 이 부분을 추가

      // response 객체가 배열이 아닌 경우에도 배열로 처리
      let responseArray = Array.isArray(response) ? response : [response];

      for (let i = 0; i < responseArray.length; i++) {
        let image = responseArray[i].image;
        let id = responseArray[i].id;
        let title = responseArray[i].title;
        let place = responseArray[i].place;
        let date = responseArray[i].date;
        let totalPrice = responseArray[i].totalPrice;
        let count = responseArray[i].count;

        let temp = `<h5 class="card-header">예매완료</h5>
                    <div class="card-body" id="getresv">
                    <ul class="info">
                      <li class="infoItem"><strong class="infoLabel"></strong>
                        <div class="infoDesc">
                          <img src="https://gykimagebucket.s3.ap-northeast-2.amazonaws.com/uploaded-image/${image}"
                        </div>
                      </li>
                      <li class="infoItem"><strong class="infoLabel">예매 번호 : </strong>
                        <div class="infoDesc" id="resvId">${id}</div>
                      </li>
                      <li class="infoItem"><strong class="infoLabel">공연 제목 : </strong>
                        <div class="infoDesc">${title}</div>
                      </li>
                      <li class="infoItem"><strong class="infoLabel">공연 장소 : </strong>
                        <div class="infoDesc"><p class="infoText">${place}</p></div>
                      </li>
                      <li class="infoItem infoDate"><strong class="infoLabel">공연 날짜 : </strong>
                        <div class="infoDesc"><p class="infoText">${date}</p></div>
                      </li>
                      <li class="infoItem infoPrice"><strong class="infoLabel">총 가격 : </strong>
                        <div class="infoDesc"><p class="infoText">${totalPrice}원</p></div>
                      </li>
                      <li class="infoItem"><strong class="infoLabel">매수 : </strong>
                        <div class="infoDesc"><p class="infoText">${count}매</p></div>
                      </li>
                       <li class="infoItem">
                        <button type="button" class="btn btn-danger cancelResvBtn" data-resv-id="${id}">예매 취소</button>
                       </li>
                    </ul>
                  </div>`;
        $('#getmypage').append(temp);


        disableCancelBtnIfPast(date, $(`button[data-resv-id="${id}"]`));

        // 예매 취소 버튼 클릭 이벤트 추가
        $('.cancelResvBtn').off('click').on('click', function () {
          let resvId = $(this).data('resv-id');
          if (confirm("정말로 예매를 취소하시겠습니까?")) {
            $.ajax({
              url: `/api/neticket/reservations/${resvId}`,
              type: "DELETE",
              success: function (response) {
                alert("예매가 취소되었습니다.");
                // window.location.href = "/neticket/user";
                showMyPage();
              },
              error: function (jqXHR, textStatus, errorThrown) {
                alert("예매 취소에 실패했습니다. 다시 시도해주세요.");
              },
            });
          }
        });
        // 오늘날짜랑 비교해서 행사일이 오늘이거나 오늘보다 이전이면 예매취소 버튼 비활성화
        function disableCancelBtnIfPast(date, btn) {
          let eventday = new Date(date);
          let today = new Date();

          if (eventday <= today) {
            btn.prop('disabled', true);
          } else {
            btn.prop('disabled', false);
          }
        }

      }
    }
  });
}
