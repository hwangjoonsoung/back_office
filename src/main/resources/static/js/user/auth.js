$(document).ready(function () {
    // Remember Me 기능: 이전에 저장된 이메일 불러오기
    const rememberedEmail = localStorage.getItem('rememberedEmail');
    if (rememberedEmail) {
        $('#email').val(rememberedEmail);
        $('#remember-me').prop('checked', true);
    }

    // 폼 제출 이벤트 처리
    $('#login_form').on('submit', function (e) {
        e.preventDefault();

        // 입력값 가져오기
        const email = $('#email').val().trim();
        const password = $('#password').val();
        const rememberMe = $('#remember-me').is(':checked');

        // 클라이언트 검증
        if (!email) {
            alert('이메일을 입력해주세요.');
            $('#email').focus();
            return;
        }

        if (!password) {
            alert('비밀번호를 입력해주세요.');
            $('#password').focus();
            return;
        }

        // Remember Me 처리
        if (rememberMe) {
            localStorage.setItem('rememberedEmail', email);
        } else {
            localStorage.removeItem('rememberedEmail');
        }

        // API 요청 데이터 준비
        const loginData = {
            email: email,
            password: password
        };

        // 버튼 비활성화 (중복 제출 방지)
        const $submitBtn = $('button[type="button"]');
        const originalText = $submitBtn.text();
        $submitBtn.prop('disabled', true).text('로그인 중...');

        // 로그인 API 호출
        $.ajax({
            url: '/api/auth/login',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(loginData),
            success: function (response) {
                // 로그인 성공 - 대시보드로 리다이렉트
                // 토큰은 서버에서 자동으로 쿠키에 저장됨
                window.location.href = '/dashboard/';
            },
            error: function (xhr) {
                // 에러 처리
                let errorMessage = '로그인에 실패했습니다.';

                if (xhr.status === 400) {
                    // 검증 오류
                    errorMessage = '이메일과 비밀번호를 확인해주세요.';
                } else if (xhr.status === 401) {
                    // 인증 실패
                    errorMessage = '이메일 또는 비밀번호가 올바르지 않습니다.';
                } else if (xhr.status === 403) {
                    // 권한 없음 (계정 비활성화 등)
                    errorMessage = '접근이 거부되었습니다. 관리자에게 문의하세요.';
                } else if (xhr.status === 500) {
                    errorMessage = '서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.';
                }

                alert(errorMessage);

                // 버튼 다시 활성화
                $submitBtn.prop('disabled', false).text(originalText);

                // 비밀번호 필드 초기화
                $('#password').val('').focus();
            }
        });
    });

    // Sign In 버튼 클릭 시 폼 제출
    $('button[type="button"]').on('click', function () {
        $('#login_form').submit();
    });

    // 폼 제출 이벤트 처리
    $('#regist_user_form').on('submit', function (e) {
        e.preventDefault();

        // 입력값 가져오기
        const name = $('#name').val().trim();
        const email = $('#email').val().trim();
        const password = $('#password').val();
        const passwordConfirm = $('#password_confirm').val();

        // 클라이언트 검증
        if (!name) {
            alert('이름을 입력해주세요.');
            $('#name').focus();
            return;
        }

        if (!email) {
            alert('이메일을 입력해주세요.');
            $('#email').focus();
            return;
        }

        // 이메일 형식 검증
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            alert('올바른 이메일 형식이 아닙니다.');
            $('#email').focus();
            return;
        }

        if (!password) {
            alert('비밀번호를 입력해주세요.');
            $('#password').focus();
            return;
        }

        if (password.length < 4) {
            alert('비밀번호는 최소 4자 이상이어야 합니다.');
            $('#password').focus();
            return;
        }

        if (password !== passwordConfirm) {
            alert('비밀번호가 일치하지 않습니다.');
            $('#password_confirm').focus();
            return;
        }

        // API 요청 데이터 준비
        const registData = {
            name: name,
            email: email,
            password: password
        };

        // 버튼 비활성화 (중복 제출 방지)
        const $submitBtn = $('button[type="submit"]');
        $submitBtn.prop('disabled', true).text('가입 중...');

        // 회원가입 API 호출
        $.ajax({
            url: '/api/user',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(registData),
            success: function (response) {
                alert('회원가입이 완료되었습니다! 로그인 페이지로 이동합니다.');
                // 로그인 페이지로 리다이렉트
                window.location.href = '/user/login';
            },
            error: function (xhr) {
                // 에러 처리
                let errorMessage = '회원가입에 실패했습니다.';

                if (xhr.status === 400) {
                    // 검증 오류
                    if (xhr.responseJSON && xhr.responseJSON.message) {
                        errorMessage = xhr.responseJSON.message;
                    } else {
                        errorMessage = '입력 정보를 확인해주세요.';
                    }
                } else if (xhr.status === 409) {
                    errorMessage = '이미 등록된 이메일입니다.';
                } else if (xhr.status === 500) {
                    errorMessage = '서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.';
                }

                alert(errorMessage);

                // 버튼 다시 활성화
                $submitBtn.prop('disabled', false).html(`
                        <span class="absolute inset-y-0 left-0 flex items-center pl-3">
                            <span class="material-icons text-white/70 group-hover:text-white transition-colors text-lg">check_circle</span>
                        </span>
                        가입하기
                    `);
            }
        });
    });
});

function logout() {
    const userId = /*[[${userid}]]*/ '';
    $.ajax({
        url: `/api/auth/${userId}/logout`,
        type: 'POST',
        contentType: 'application/json',
        success: function (response) {
            window.location.href = '/user/login';
        },
        error: function (xhr) {
            // 에러 처리
            let errorMessage = '로그아웃을 실패했습니다.';
            alert(errorMessage)
        }
    });
}