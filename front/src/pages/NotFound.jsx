import { Link } from "react-router-dom";
function NotFound() {
    return (
        <>
            <h1>Page not found</h1>
            <Link to="/">Go to homepage</Link>
        </>
    )
}
export default NotFound;