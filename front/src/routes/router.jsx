import { createBrowserRouter } from "react-router-dom";
import App from "../App";
import NotFound from "../pages/NotFound";
import Login from "../pages/Login";
import Register from "../pages/Register";

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
        },
        {
            path: "/movie/:id",
        },
        {
            path: "/series/:id",
        },
        {
            path: "/add",
        },
        {
            path: "/add/movie",
        },
        {
            path: "/add/series",
        },
        ],
    },
    {
        path: "*",
        element: <NotFound/>
    }
])
export default router;