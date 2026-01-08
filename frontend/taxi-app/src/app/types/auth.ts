export interface UserLoginDto {
  email: string;
  password: string;
}

export interface UserRegisterDto {
  email: string;
  password: string;
  lastname: string;
  firstname: string;
  address: string;
  phonenumber: string;
}

export interface LoginResponseDto {
  accessToken: string;
  email: string;
  firstname: string;
  lastname: string;
  role: string;
}
