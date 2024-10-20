document.getElementById('backBtn').addEventListener('click', function() {
    const url = this.getAttribute('data-url');
    window.location.href = url;
});
document.getElementById('nextBtn').addEventListener('click', function() {
    const url = this.getAttribute('data-url');
    window.location.href = url;
});