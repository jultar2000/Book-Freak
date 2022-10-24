export function getItemFromLocalStorage(type) {
    return localStorage.getItem(type)
}

export function setItemToLocalStorage(key, value) {
    localStorage.setItem(key, value)
}
