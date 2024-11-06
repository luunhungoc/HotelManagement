
;(function($, window, document, undefined) {

    "use strict";

    var pluginName = "select",
        defaults = {
            multiple: false,
            search: false,
            listLabel: false,
            useCtrl: false
        };

    function Plugin(element, options) {
        this.element = element;
        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this.elements = {};

        this.init();
    }

    $.extend(Plugin.prototype, {

        init: function() {
            var $element = $(this.element);

            // get settings multiple
            if (($element.attr('multiple') !== undefined) && (this.settings.multiple !== true)) {
                this.settings.multiple = true;
            }
            // list label inside placeholder
            if(this.settings.multiple && $element.data('list-label') !== undefined) {
                this.settings.listLabel = true;
            }
            // dropdown search
            if ($element.data('dropdown-search') !== undefined) {
                this.settings.search = true;
            }
            // use ctrl to select multiple choices
            if ($element.data('useCtrl') !== undefined && $element.data('useCtrl') === true) {
                this.settings.useCtrl = true;
            }
            // hide element right from the start
            $element.hide();
            // $element.addClass('visually-hidden');

            // get initial placeholder
            var initial = this.getPlaceholder($element);

            // find selected choices
            var $selected = $(this.element).find(':selected');

            // multiple check
            if (this.settings.multiple) {
                // if 1 or more choices
                if ($selected.length > 0) {
                    initial = '';
                    // check settings for listlabel
                    if (this.settings.listLabel) {
                        // for each choice update placeholder with choice text
                        $.each($selected, function(index) {
                            var sel = $(this).text();
                            if (index < $selected.length - 1) {
                                initial += sel + ', ';
                            } else {
                                initial += sel;
                            }
                        });
                    } else {
                        // if a single item is selected set placeholder that choice's text
                        // set total selected items as placeholder
                        if ($selected.length == 1) {
                            initial = $selected.text();
                        } else {
                            initial = $selected.length + ' selected';
                        }
                    }
                }
            } else {
                // is single selected then update placeholder
                if ($selected.eq(0).val()) {
                    initial = $selected.text();
                }
            }

            // create wrapper & other components (placeholder, label)
            this.elements.$wrap = $element.parent(); //$('<div class="select" />');
            this.elements.$label = $('<div/>').addClass('form-control form-control--select').html(initial);
            this.elements.$panel = $('<div/>').addClass('select__panel');
            this.elements.$list = $('<ul/>').addClass('select__list');

            this.elements.itemClass = 'select__list-item';

            this.elements.$search = $('<input/>').attr({
                'type': 'text',
                'placeholder': 'Type here to search'
            }).addClass('form-control select__search');

            this.elements.$nolist = $('<li/>').addClass('select__nolist select__list-item').text('No results found');

            this.elements.$panel.append(this.elements.$list);
            this.elements.$wrap.append(this.elements.$label);
            this.elements.$wrap.append(this.elements.$panel);

            if (this.settings.multiple && this.settings.listLabel) {
                this.elements.$label.addClass('of-ellipsis');
            }

            this.bind();
        },

        bind: function() {
            var that = this;
            var id_array = [];

            // open the panel and append the list items if clicked on the form-control label
            this.elements.$label.on('click', function() {

                if (that.elements.$panel.hasClass('is-visible')) {
                    that.elements.$panel.removeClass('is-visible');
                    return;
                }

                if ($(that.element).is(':disabled')) return;

                var options = $(that.element).find('option'), html = '';
                // reset the array contents
                id_array = [];

                $.each(options, function() {
                    if ($(this).attr('value') === undefined) {
                        return;
                    }
                    var sel = $(this).is(':selected');

                    html += '<li class="' + that.elements.itemClass + '' + (sel ? ' is-selected' : '') + '" data-id="' + $(this).attr('value') + '">' + $(this).text() + '</li>';

                    if (sel) {
                        var sel_id = parseInt($(this).val());
                        id_array.push(sel_id);
                    }
                });

                if (that.settings.search) {
                    that.elements.$label.addClass('hidden');
                    that.elements.$wrap.find('.select__panel').before(that.elements.$search);
                    that.elements.$search.val('').trigger('focus');
                }

                that.elements.$list.html(html);
                that.elements.$panel.addClass('is-visible');
            });

            // Close the panel if 'click anywhere'

            $(document).on('click', function(e) {
                if (!$(e.target).is(that.elements.$label)) {
                    if (!$(e.target).is(that.elements.$search)) {

                        // multiple check require event
                        if (that.multipleCheckOption(e)) {
                            that.elements.$panel.removeClass('is-visible');
                        }

                        if (that.settings.search) {
                            that.elements.$search.remove();
                            that.elements.$label.removeClass('hidden');
                        }
                    }
                }
            });

            // Select an item via a mouse click

            this.elements.$panel.on('click', 'li', function(e) {
                var id = $(this).data('id'),
                    title = $(this).text();

                // muliple check
                if (that.multipleCheckOption(e)) { // single

                    that.select(id, title);
                    id_array = [];

                } else { // multiple

                    if (!$(this).hasClass("is-selected")) {

                        $(this).addClass('is-selected');
                        id_array.push(id);
                        that.select(id_array, title);

                    } else {

                        $(this).removeClass('is-selected');

                        var pos = $.inArray(id, id_array);
                        if (pos > -1) {
                            id_array.splice(pos, 1);
                        }

                        that.select(id_array, title);

                    }
                }
            });

            // Basic search in dropdown

            this.elements.$wrap.on('keyup', '.select__search', function(e) {
                if (that.settings.search) {
                    var code = e.keyCode || e.which;
                    if (code == '9') return;

                    var $input = that.elements.$search,
                        inputContent = $input.val().toLowerCase();
                    var options = that.elements.$panel.find('li');

                    if (inputContent.length >= 2) {
                        var searchEmpty = 0;

                        $.each(options, function() {
                            var optionValue = $(this).text().toLowerCase();
                            if (optionValue === undefined) {
                                return;
                            }
                            if (!optionValue.includes(inputContent)) {
                                $(this).hide();
                                searchEmpty += 1;
                                if (searchEmpty == options.length) {
                                    that.elements.$list.append(that.elements.$nolist);
                                    that.elements.$nolist.show();
                                }
                            } else {
                                $(this).show();
                            }
                        });
                    } else {
                        if (inputContent.length === 0) {
                            that.elements.$nolist.hide();
                            $.each(options, function() {
                                $(this).show();
                            });
                        }
                    }
                }
            });

            // Change event for selected item

            $(this.element).on('change', function() {

                var text = that.getPlaceholder($(this));
                var $selected = $(this).find(':selected');

                if (that.settings.multiple) {
                    if ($selected.length > 0) {
                        text = '';
                        if (that.settings.listLabel) {
                            $.each($selected, function(index) {
                                var sel = $(this).text();
                                if (index < $selected.length - 1) {
                                    text += sel + ', ';
                                } else {
                                    text += sel;
                                }
                            });
                        } else {
                            if ($selected.length == 1) {
                                text = $selected.text();
                            } else {
                                text = $selected.length + ' selected';
                            }
                        }
                    }
                } else {
                    if ($selected.eq(0).val()) {
                        text = $selected.text();
                    }
                }

                if (that.settings.search) {
                    that.elements.$search.remove();
                    that.elements.$label.removeClass('hidden');
                }
                that.elements.$label.text(text);
            });

        },

        select: function(id) {
            $(this.element).val(id).trigger('change');
        },
        getPlaceholder: function(el) {
            var text = "";
            // for single display first item
            if (!this.settings.multiple) {
                text = el.find('option').first().text();
            } else { // for multiple get placeholder
                text = el.attr('placeholder');
            }
            return text;
        },
        multipleCheckOption: function(event) {
            var testAction;
            var item = $('.' + this.elements.itemClass)
            if (this.settings.useCtrl) {
                testAction = event.ctrlKey;
            } else {
                testAction = $(event.target).is(item);
            }
            return (testAction && !this.settings.multiple) || (!testAction && this.settings.multiple) || (!testAction && !this.settings.multiple);
        }

    });

    $.fn[pluginName] = function(options) {
        return this.each(function() {
            $.data(this, "plugin_" + pluginName, new Plugin(this, options));
        });
    };

})(jQuery, window, document);

$(function() {
	$('select').select();
})