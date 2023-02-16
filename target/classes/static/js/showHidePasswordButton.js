'use strict';


/**
 * Show/hide password button on login page
 */
$(function() {
  $('.hide-show').show();
  $('.hide-show span').addClass('show');

  $('.hide-show span').click(function() {
    if ($('.hide-show span').hasClass('show')) {
      $('.hide-show span').text('Hide');
      $('input[name="password"]').attr('type', 'text');
      $('.hide-show span').removeClass('show');
    } else {
      $('.hide-show span').text('Show');
      $('input[name="password"]').attr('type', 'password');
      $('.hide-show span').addClass('show');
    }
  });

  $('form button[type="submit"]').on('click', function() {
    $('.hide-show span').text('Show').addClass('show');
    $('.hide-show').parent().find('input[name="password"]')
        .attr('type', 'password');
  });
});
