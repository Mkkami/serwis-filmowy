import { createBrowserRouter } from "react-router-dom";
import App from "../App";
import NotFound from "../pages/NotFound";
import Login from "../pages/Login";
import Register from "../pages/Register";
import AddMovie from "../pages/AddMovie";
import AddSeries from "../pages/AddSeries";

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
            element: <AddMovie/>
        },
        {
            path: "/add/series",
            element: <AddSeries/>
        },
        ],
    },
    {
        path: "*",
        element: <NotFound/>
    }
])
export default router;