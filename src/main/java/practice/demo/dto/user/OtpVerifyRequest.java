    package practice.demo.dto.user;

    public class OtpVerifyRequest {

        private String email;
        private String otp; // verify ke time use hoga

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }
    }
