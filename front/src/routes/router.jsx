import { createBrowserRouter } from "react-router-dom";
import App from "../App";
import NotFound from "../pages/NotFound";
import Login from "../pages/Login";
import Register from "../pages/Register";
import MovieForm from "../pages/MovieForm";
import SeriesForm from "../pages/SeriesForm";
import Movie from "../pages/Movie";
import Series from "../pages/Series";
import SearchResult from "../pages/SearchResult"

const router = createBrowserRouter([
    {
        path: "/",
        element: <App />,
        children: [
        { 
            path: "/login",
            element: <Login />
        },
        {
            path: "/register",
            element: <Register />
        },
        {
            path: "/search",
            element: <SearchResult />
        },
        {
            path: "/film/:id",
            element: <Movie />
        },
        {
            path: "/series/:id",
            element: <Series />
        },
        {
            path: "/add",
        },
        {
            path: "/add/film",
            element: <MovieForm/>
        },
        {
            path: "/add/series",
            element: <SeriesForm/>
        },
        ],
    },
    {
        path: "*",
        element: <NotFound/>
    }
])
export default router;