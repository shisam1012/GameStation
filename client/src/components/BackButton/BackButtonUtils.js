export function goBack(navigate, location) {
    if (location.state?.from) {
        navigate(location.state.from);
    } else {
        navigate('/');
    }
}
