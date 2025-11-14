
function Reviews({reviews}) {

    if (reviews === null) {
        return (
            <div className="reviews">
                No reviews yet.
            </div>
        )
    }
    
    return (
        <div className="reviews">
            <h2>Reviews</h2>
            {reviews.map(rev => 
                <div key={rev.id} className="review-card">
                    <div className="score">
                        <p>10/10</p>
                    </div>
                    <div className="description">
                        <p className="username">{rev.user}</p>
                        <p className="comment">{rev.comment}</p>
                    </div>
                </div>
            )}
            </div>
    )
}
export default Reviews;