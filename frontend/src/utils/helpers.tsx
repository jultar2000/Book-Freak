export function getItemFromLocalStorage(type: string) {
    return localStorage.getItem(type)
}

export function setItemToLocalStorage(key: string, value: string) {
    localStorage.setItem(key, value)
}
