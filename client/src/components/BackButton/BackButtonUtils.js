//This function is called when the client press the back button
export function goBack(navigate, location) {
    if (location.state?.from) {
        navigate(location.state.from);
    } else {
        navigate('/');
    }
}
