export interface ButtonProps {
    type: string,
    style?: any,
    onClick: React.MouseEventHandler<HTMLButtonElement>,
    disabled?: boolean,
    text: string
}