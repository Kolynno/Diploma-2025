document.getElementById('newParticipantBtn').addEventListener('click', function() {
    window.location.href = '/r';
});

const blocks = document.querySelectorAll('.block');
blocks.forEach(block => {
    block.addEventListener('click', function() {
        const url = this.getAttribute('data-url');
        window.location.href = url;
    });
});
