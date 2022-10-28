const USERNAME_REGEX = /^\[A-z\][A-z0-9-_]{3,23}$/;
const PASSWORD_REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%]).{8,24}$/;
const EMAIL_REGEX = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

export function validateUsername(username: string) {
    return USERNAME_REGEX.test(username)
}

export function validateEmail(email: string): boolean {
    return EMAIL_REGEX.test(email)
}

export function validatePassword(password: string) {
    return PASSWORD_REGEX.test(password)
}
