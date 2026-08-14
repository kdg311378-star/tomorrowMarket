import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/axios";
import "./SignupPage.css";

const INTEREST_OPTIONS = [
  "반도체", "2차전지", "바이오", "AI/플랫폼", "금융", "배당주", "미국주식", "암호화폐"
];

function SignupPage() {
  const navigate = useNavigate();
  const [selectedInterests, setSelectedInterests] = useState<string[]>([]);
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    passwordConfirm: "",
    name: "",
    birthdate: ""
  });
  const [errorMsg, setErrorMsg] = useState("");
  
  // Birthdate State
  const [birthYear, setBirthYear] = useState("");
  const [birthMonth, setBirthMonth] = useState("");
  const [birthDay, setBirthDay] = useState("");

  const currentYear = new Date().getFullYear();
  const years = Array.from({ length: 100 }, (_, i) => currentYear - i);
  const months = Array.from({ length: 12 }, (_, i) => i + 1);
  const days = Array.from({ length: 31 }, (_, i) => i + 1);

  useEffect(() => {
    if (birthYear && birthMonth && birthDay) {
      setFormData(prev => ({
        ...prev,
        birthdate: `${birthYear}-${birthMonth.padStart(2, '0')}-${birthDay.padStart(2, '0')}`
      }));
    } else {
      setFormData(prev => ({ ...prev, birthdate: "" }));
    }
  }, [birthYear, birthMonth, birthDay]);

  // Email Verification State
  const [otpCode, setOtpCode] = useState("");
  const [isCodeSent, setIsCodeSent] = useState(false);
  const [isEmailVerified, setIsEmailVerified] = useState(false);
  const [timeLeft, setTimeLeft] = useState(180); // 3 minutes
  const [timerActive, setTimerActive] = useState(false);

  // Password validation state
  const [passwordStrength, setPasswordStrength] = useState("");
  const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,20}$/;

  useEffect(() => {
    let interval: NodeJS.Timeout;
    if (timerActive && timeLeft > 0) {
      interval = setInterval(() => {
        setTimeLeft((prev) => prev - 1);
      }, 1000);
    } else if (timeLeft === 0) {
      setTimerActive(false);
      setIsCodeSent(false); // Reset if time expires
    }
    return () => clearInterval(interval);
  }, [timerActive, timeLeft]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });

    if (name === "password") {
      if (!value) {
        setPasswordStrength("");
      } else if (passwordRegex.test(value)) {
        setPasswordStrength("안전한 비밀번호입니다.");
      } else {
        setPasswordStrength("비밀번호는 8~20자리이며, 영문/숫자/특수문자를 모두 포함해야 합니다.");
      }
    }
  };

  const toggleInterest = (interest: string) => {
    setSelectedInterests(prev => 
      prev.includes(interest) 
        ? prev.filter(i => i !== interest) 
        : [...prev, interest]
    );
  };

  const handleSendCode = async () => {
    if (!formData.email) {
      setErrorMsg("이메일을 입력해주세요.");
      return;
    }
    setErrorMsg("");
    try {
      await api.post("/auth/email/send", { email: formData.email });
      setIsCodeSent(true);
      setTimerActive(true);
      setTimeLeft(180);
      alert("인증번호가 발송되었습니다. 3분 이내에 입력해주세요.");
    } catch (error: any) {
      setErrorMsg(error.response?.data?.message || "인증번호 발송에 실패했습니다.");
    }
  };

  const handleVerifyCode = async () => {
    if (!otpCode) return;
    setErrorMsg("");
    try {
      await api.post("/auth/email/verify", { email: formData.email, code: otpCode });
      setIsEmailVerified(true);
      setTimerActive(false);
      alert("이메일 인증이 완료되었습니다.");
    } catch (error: any) {
      setErrorMsg(error.response?.data?.message || "인증번호가 올바르지 않습니다.");
    }
  };

  const handleSignup = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrorMsg("");

    if (!isEmailVerified) {
      setErrorMsg("이메일 인증을 먼저 완료해주세요.");
      return;
    }

    if (!passwordRegex.test(formData.password)) {
      setErrorMsg("비밀번호 양식을 확인해주세요.");
      return;
    }

    if (formData.password !== formData.passwordConfirm) {
      setErrorMsg("비밀번호가 일치하지 않습니다.");
      return;
    }

    try {
      const payload = {
        email: formData.email,
        password: formData.password,
        name: formData.name,
        birthdate: formData.birthdate
      };
      await api.post("/auth/signup", payload);
      alert("회원가입이 완료되었습니다!");
      navigate("/login");
    } catch (error: any) {
      setErrorMsg(error.response?.data?.message || "회원가입 중 오류가 발생했습니다.");
    }
  };

  const formatTime = (seconds: number) => {
    const m = Math.floor(seconds / 60);
    const s = seconds % 60;
    return `${m}:${s < 10 ? '0' : ''}${s}`;
  };

  return (
    <div className="signup-container">
      {/* 좌측 비주얼 섹션 */}
      <section className="signup-visual-section">
        <h1>나만의<br /><span>투자 아이덴티티</span><br />설정하기</h1>
        <p>
          내일장은 당신의 투자 성향을 분석하여 맞춤형 인사이트를 제공합니다. 
          지금 바로 합류하여 AI 기반의 스마트한 투자를 경험하세요.
        </p>
      </section>

      {/* 우측 폼 섹션 */}
      <section className="signup-form-section">
        <div className="signup-card">
          <h2>회원가입</h2>
          <p className="subtitle">내일장과 함께 성공적인 투자 여정을 시작하세요.</p>

          <form onSubmit={handleSignup} autoComplete="off">
            {/* 브라우저 자동완성 덮어쓰기 방지 트랩 */}
            <input type="text" name="fake-email-trap" style={{ display: "none" }} aria-hidden="true" />
            <input type="password" name="fake-pw-trap" style={{ display: "none" }} aria-hidden="true" />
            
            {errorMsg && <div style={{ color: "red", marginBottom: "15px", fontSize: "14px" }}>{errorMsg}</div>}
            
            <div className="signup-input-group">
              <label>이메일</label>
              <div style={{ display: "flex", gap: "10px" }}>
                <input type="email" name="email" value={formData.email} onChange={handleInputChange} placeholder="example@tomorrow.com" required disabled={isEmailVerified} autoComplete="off" style={{ flex: 1 }} />
                <button type="button" onClick={handleSendCode} disabled={isEmailVerified || (timerActive && timeLeft > 170)} style={{ padding: "0 15px", background: "#3b82f6", color: "white", border: "none", borderRadius: "4px", cursor: "pointer", whiteSpace: "nowrap" }}>
                  {isCodeSent ? "재전송" : "인증번호 받기"}
                </button>
              </div>
            </div>

            {isCodeSent && !isEmailVerified && (
              <div className="signup-input-group">
                <label>인증번호 입력 <span style={{ color: "red" }}>({formatTime(timeLeft)})</span></label>
                <div style={{ display: "flex", gap: "10px" }}>
                  <input type="text" value={otpCode} onChange={(e) => setOtpCode(e.target.value)} placeholder="6자리 인증번호" required autoComplete="off" style={{ flex: 1 }} />
                  <button type="button" onClick={handleVerifyCode} style={{ padding: "0 15px", background: "#10b981", color: "white", border: "none", borderRadius: "4px", cursor: "pointer", whiteSpace: "nowrap" }}>
                    인증 확인
                  </button>
                </div>
              </div>
            )}

            <div className="signup-input-group">
              <label>비밀번호</label>
              <input type="password" name="password" value={formData.password} onChange={handleInputChange} placeholder="8~20자, 영문/숫자/특수문자 포함" required autoComplete="new-password" />
              {passwordStrength && (
                <div style={{ marginTop: "5px", fontSize: "12px", color: formData.password.match(passwordRegex) ? "#10b981" : "#ef4444" }}>
                  {passwordStrength}
                </div>
              )}
            </div>

            <div className="signup-input-group">
              <label>비밀번호 확인</label>
              <input type="password" name="passwordConfirm" value={formData.passwordConfirm} onChange={handleInputChange} placeholder="비밀번호를 다시 입력하세요" required autoComplete="new-password" />
              {formData.passwordConfirm && (
                <div style={{ marginTop: "5px", fontSize: "12px", color: formData.password === formData.passwordConfirm ? "#10b981" : "#ef4444" }}>
                  {formData.password === formData.passwordConfirm ? "비밀번호가 일치합니다." : "비밀번호가 일치하지 않습니다."}
                </div>
              )}
            </div>

            <div className="signup-input-group">
              <label>닉네임(이름)</label>
              <input type="text" name="name" value={formData.name} onChange={handleInputChange} placeholder="활동하실 이름을 입력하세요" required autoComplete="off" />
            </div>

            <div className="signup-input-group">
              <label>생년월일</label>
              <div style={{ display: "flex", gap: "10px" }}>
                <select value={birthYear} onChange={(e) => setBirthYear(e.target.value)} required style={{ flex: 1, padding: "12px", border: "1px solid #334155", borderRadius: "8px", background: "#0f172a", color: "white" }}>
                  <option value="">연도</option>
                  {years.map(y => <option key={y} value={y}>{y}년</option>)}
                </select>
                <select value={birthMonth} onChange={(e) => setBirthMonth(e.target.value)} required style={{ flex: 1, padding: "12px", border: "1px solid #334155", borderRadius: "8px", background: "#0f172a", color: "white" }}>
                  <option value="">월</option>
                  {months.map(m => <option key={m} value={m}>{m}월</option>)}
                </select>
                <select value={birthDay} onChange={(e) => setBirthDay(e.target.value)} required style={{ flex: 1, padding: "12px", border: "1px solid #334155", borderRadius: "8px", background: "#0f172a", color: "white" }}>
                  <option value="">일</option>
                  {days.map(d => <option key={d} value={d}>{d}일</option>)}
                </select>
              </div>
            </div>

            <div className="interest-group">
              <span className="interest-label">관심 분야 (다중 선택 가능)</span>
              <div className="interest-tags">
                {INTEREST_OPTIONS.map(interest => (
                  <div 
                    key={interest}
                    className={`interest-tag ${selectedInterests.includes(interest) ? "active" : ""}`}
                    onClick={() => toggleInterest(interest)}
                  >
                    {interest}
                  </div>
                ))}
              </div>
            </div>

            <button type="submit" className="signup-submit-btn" disabled={!isEmailVerified}>내일장 시작하기</button>

            <div className="login-link-container">
              이미 계정이 있으신가요? 
              <span onClick={() => navigate("/login")}>로그인하러 가기</span>
            </div>
          </form>
        </div>
      </section>
    </div>
  );
}

export default SignupPage;