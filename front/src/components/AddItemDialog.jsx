import { useNavigate } from "react-router-dom";
import "../styles/components/AddItemDialog.css"
import { forwardRef, useImperativeHandle, useRef } from "react";

const AddItemDialog = forwardRef((props, ref) => {
    const navigate = useNavigate();
    const dialogRef = useRef(null);

    useImperativeHandle(ref, () => ({
        showModal() {
            dialogRef.current?.showModal();
        },
        close() {
            dialogRef.current?.close();
        },
    }));

    const handleNavigate = (path) => {
        dialogRef.current?.close();
        navigate(path);
    }


    return (
        <dialog ref={dialogRef}>
            <button className="close-btn" onClick={() => dialogRef.current?.close()}>X</button>
            <div>
                <button onClick={() => handleNavigate("/add/film")}>Film</button>
                <button onClick={() => handleNavigate("/add/series")}>Series</button>
            </div>
        </dialog>
    )
})
export default AddItemDialog;