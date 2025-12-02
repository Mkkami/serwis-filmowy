import { useRef, useState } from "react";
import "../styles/components/Review.css"
import { addReview } from "../api/api";

function Reviews({ reviews, type, id }) {
    const dialogRef = useRef(null);
    const [rating, setRating] = useState(0);
    const [comment, setComment] = useState("");
    const [error, setError] = useState("");

    const handleAddReview = () => {
        dialogRef.current?.showModal();
    }

    const handleClose = () => {
        dialogRef.current?.close();
    }

    const handleSubmit = async (e) => {
        e.preventDefault();

        const res = await addReview(id, type.toLowerCase(), rating, comment);
        if (!res.ok) {
            setError(await res.text());
            return;
        }
        setError("");
        handleClose();
    }

    const handleChangeRating = (e) => {
        if (e.target.value > 10) {
            e.target.value = 10
        }
        if (e.target.value < 0) {
            e.target.value = 0
        }
        setRating(e.target.value);
    }

    return (
        <>
            <div className="reviews">
                <h2>Reviews</h2>
                <button onClick={handleAddReview}>Add</button>
                {reviews.map(rev =>
                    <div key={rev.id} className="review-card">
                        <div className="score">
                            <p>{rev.rating}/10</p>
                        </div>
                        <div className="description">
                            <p className="username">{rev.user}</p>
                            <p className="comment">{rev.comment}</p>
                        </div>
                    </div>
                )}
            </div>
            <dialog ref={dialogRef}>
                <button onClick={handleClose}>X</button>
                <form onSubmit={handleSubmit}>
                    <div>
                        <label htmlFor="rating">Rating</label>
                        <input id="rating" name="rating" type="number" onChange={(e) => handleChangeRating(e)} />/10
                    </div>
                    <div>
                        <label htmlFor="comment" >Comment</label>
                        <input id="comment" name="comment" type="text" onChange={(e) => setComment(e.target.value)} />
                    </div>
                    {error && <p className="error">{error}</p>}
                    <button>Submit</button>
                </form>
            </dialog>
        </>

    )
}
export default Reviews;